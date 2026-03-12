package com.example.algoviz.ui.screens.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algoviz.data.remote.ChatMessage
import com.example.algoviz.domain.repository.AIRepository
import com.example.algoviz.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AIChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentLanguage: String = "en-IN"
)

@HiltViewModel
class AITutorViewModel @Inject constructor(
    private val aiRepository: AIRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AIChatUiState())
    val uiState: StateFlow<AIChatUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            if (user != null) {
                _uiState.value = _uiState.value.copy(currentLanguage = user.sarvamLanguage)
            }
        }
    }

    fun sendMessage(content: String) {
        val currentMessages = _uiState.value.messages.toMutableList()
        val userMessage = ChatMessage(role = "user", content = content)
        currentMessages.add(userMessage)
        
        _uiState.value = _uiState.value.copy(messages = currentMessages, isLoading = true, error = null)

        viewModelScope.launch {
            val result = aiRepository.getAIResponse(currentMessages, _uiState.value.currentLanguage)
            result.onSuccess { aiContent ->
                val aiMessage = ChatMessage(role = "assistant", content = aiContent)
                currentMessages.add(aiMessage)
                _uiState.value = _uiState.value.copy(messages = currentMessages, isLoading = false)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun setLanguage(language: String) {
        _uiState.value = _uiState.value.copy(currentLanguage = language)
    }
}