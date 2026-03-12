package com.example.algoviz.ui.screens.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algoviz.domain.model.QuizQuestion
import com.example.algoviz.domain.repository.AuthRepository
import com.example.algoviz.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val score: Int = 0,
    val isQuizFinished: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun loadQuiz(topicId: String) {
        viewModelScope.launch {
            // In a real app, fetch from a repository. For now, use mock data.
            val mockQuestions = getMockQuestions(topicId)
            _uiState.value = _uiState.value.copy(questions = mockQuestions, isLoading = false)
        }
    }

    fun selectOption(index: Int) {
        if (!_uiState.value.isAnswerSubmitted) {
            _uiState.value = _uiState.value.copy(selectedOptionIndex = index)
        }
    }

    fun submitAnswer() {
        val state = _uiState.value
        if (state.selectedOptionIndex != null && !state.isAnswerSubmitted) {
            val isCorrect = state.selectedOptionIndex == state.questions[state.currentQuestionIndex].correctAnswerIndex
            _uiState.value = state.copy(
                isAnswerSubmitted = true,
                score = if (isCorrect) state.score + 1 else state.score
            )
        }
    }

    fun nextQuestion(topicId: String) {
        val state = _uiState.value
        if (state.currentQuestionIndex < state.questions.size - 1) {
            _uiState.value = state.copy(
                currentQuestionIndex = state.currentQuestionIndex + 1,
                selectedOptionIndex = null,
                isAnswerSubmitted = false
            )
        } else {
            finishQuiz(topicId)
        }
    }

    private fun finishQuiz(topicId: String) {
        viewModelScope.launch {
            val state = _uiState.value
            val finalScore = (state.score.toFloat() / state.questions.size.toFloat()) * 100
            val userId = authRepository.getCurrentUser()?.id ?: ""
            if (userId.isNotEmpty()) {
                progressRepository.updateQuizScore(userId, topicId, finalScore)
            }
            _uiState.value = state.copy(isQuizFinished = true)
        }
    }

    private fun getMockQuestions(topicId: String): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                "1",
                "What is the worst-case time complexity of Bubble Sort?",
                listOf("O(n)", "O(n log n)", "O(n²)", "O(1)"),
                2,
                "Bubble Sort has a worst-case complexity of O(n²) when the array is in reverse order."
            ),
            QuizQuestion(
                "2",
                "Is Bubble Sort a stable sorting algorithm?",
                listOf("Yes", "No"),
                0,
                "Yes, Bubble Sort is stable because it does not swap equal elements."
            )
        )
    }
}