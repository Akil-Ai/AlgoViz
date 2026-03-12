package com.example.algoviz.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

@Serializable
data class Quiz(
    val topicId: String,
    val questions: List<QuizQuestion>
)