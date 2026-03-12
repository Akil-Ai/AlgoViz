package com.example.algoviz.ui.screens.arena

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algoviz.domain.model.Problem
import com.example.algoviz.domain.repository.ProblemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ArenaUiState {
    object Loading : ArenaUiState()
    data class Success(val problems: List<Problem>) : ArenaUiState()
    data class Error(val message: String) : ArenaUiState()
}

@HiltViewModel
class ArenaViewModel @Inject constructor(
    private val problemRepository: ProblemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArenaUiState>(ArenaUiState.Loading)
    val uiState: StateFlow<ArenaUiState> = _uiState.asStateFlow()

    init {
        loadProblems()
    }

    fun loadProblems() {
        viewModelScope.launch {
            _uiState.value = ArenaUiState.Loading
            problemRepository.refreshProblems()
            problemRepository.getProblems().collect { problems ->
                _uiState.value = ArenaUiState.Success(problems)
            }
        }
    }
}