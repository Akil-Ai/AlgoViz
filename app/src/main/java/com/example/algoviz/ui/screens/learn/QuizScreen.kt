package com.example.algoviz.ui.screens.learn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.algoviz.ui.theme.DeepNavy
import com.example.algoviz.ui.theme.MintAccent
import com.example.algoviz.ui.theme.OrangeAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    topicId: String,
    onNavigateBack: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(topicId) {
        viewModel.loadQuiz(topicId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz: $topicId", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MintAccent)
            } else if (uiState.isQuizFinished) {
                QuizResultView(
                    score = uiState.score,
                    total = uiState.questions.size,
                    onNavigateBack = onNavigateBack
                )
            } else {
                val currentQuestion = uiState.questions[uiState.currentQuestionIndex]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    // Progress
                    LinearProgressIndicator(
                        progress = { (uiState.currentQuestionIndex + 1).toFloat() / uiState.questions.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MintAccent,
                        trackColor = MintAccent.copy(alpha = 0.1f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Question ${uiState.currentQuestionIndex + 1} of ${uiState.questions.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Question
                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Options
                    currentQuestion.options.forEachIndexed { index, option ->
                        OptionCard(
                            text = option,
                            isSelected = uiState.selectedOptionIndex == index,
                            isCorrect = uiState.isAnswerSubmitted && index == currentQuestion.correctAnswerIndex,
                            isWrong = uiState.isAnswerSubmitted && uiState.selectedOptionIndex == index && index != currentQuestion.correctAnswerIndex,
                            isEnabled = !uiState.isAnswerSubmitted,
                            onClick = { viewModel.selectOption(index) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Explanation & Next Button
                    AnimatedVisibility(visible = uiState.isAnswerSubmitted) {
                        Column {
                            Surface(
                                color = DeepNavy.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = currentQuestion.explanation,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    Button(
                        onClick = {
                            if (uiState.isAnswerSubmitted) {
                                viewModel.nextQuestion(topicId)
                            } else {
                                viewModel.submitAnswer()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (uiState.isAnswerSubmitted) MintAccent else OrangeAccent,
                            contentColor = DeepNavy
                        ),
                        enabled = uiState.selectedOptionIndex != null
                    ) {
                        Text(
                            text = if (uiState.isAnswerSubmitted) "Next Question" else "Submit Answer",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionCard(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isWrong: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isCorrect -> MintAccent
        isWrong -> MaterialTheme.colorScheme.error
        isSelected -> OrangeAccent
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }

    val backgroundColor = when {
        isCorrect -> MintAccent.copy(alpha = 0.1f)
        isWrong -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        isSelected -> OrangeAccent.copy(alpha = 0.1f)
        else -> Color.Transparent
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(enabled = isEnabled, onClick = onClick),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (isCorrect) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MintAccent)
            } else if (isWrong) {
                Icon(Icons.Filled.Close, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun QuizResultView(
    score: Int,
    total: Int,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Completed!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MintAccent
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "You scored",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "$score / $total",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onNavigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MintAccent, contentColor = DeepNavy)
        ) {
            Text("Back to Lesson", fontWeight = FontWeight.Bold)
        }
    }
}