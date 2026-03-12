package com.example.algoviz.ui.screens.arena

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algoviz.data.remote.SubmissionResult
import com.example.algoviz.domain.repository.SubmissionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditorUiState(
    val code: String = "",
    val languageId: Int = 62, // Java (OpenJDK 13.0.1)
    val isSubmitting: Boolean = false,
    val result: SubmissionResult? = null,
    val error: String? = null
)

@HiltViewModel
class CodeEditorViewModel @Inject constructor(
    private val submissionRepository: SubmissionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    fun onCodeChange(newCode: String) {
        _uiState.value = _uiState.value.copy(code = newCode)
    }

    fun onLanguageChange(newLanguageId: Int) {
        _uiState.value = _uiState.value.copy(languageId = newLanguageId)
    }

    fun submitCode(problemId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, result = null, error = null)
            val result = submissionRepository.submitCode(
                problemId = problemId,
                code = _uiState.value.code,
                languageId = _uiState.value.languageId
            )
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isSubmitting = false, result = it)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isSubmitting = false, error = it.message)
            }
        }
    }
}