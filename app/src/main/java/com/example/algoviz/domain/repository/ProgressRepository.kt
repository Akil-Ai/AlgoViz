package com.example.algoviz.domain.repository

import com.example.algoviz.domain.model.Progress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun getProgressForUser(userId: String): Flow<List<Progress>>
    suspend fun getProgressForTopic(userId: String, topicId: String): Progress?
    suspend fun markLessonCompleted(userId: String, topicId: String): Result<Unit>
    suspend fun updateQuizScore(userId: String, topicId: String, score: Float): Result<Unit>
    suspend fun markVisualizationWatched(userId: String, topicId: String): Result<Unit>
}