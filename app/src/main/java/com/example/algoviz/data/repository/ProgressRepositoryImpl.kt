package com.example.algoviz.data.repository

import com.example.algoviz.data.local.dao.ProgressDao
import com.example.algoviz.data.local.entity.ProgressEntity
import com.example.algoviz.domain.model.Progress
import com.example.algoviz.domain.repository.ProgressRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProgressRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val progressDao: ProgressDao
) : ProgressRepository {

    override fun getProgressForUser(userId: String): Flow<List<Progress>> {
        return progressDao.getProgressByUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProgressForTopic(userId: String, topicId: String): Progress? {
        return progressDao.getProgress(userId, topicId)?.toDomain()
    }

    override suspend fun markLessonCompleted(userId: String, topicId: String): Result<Unit> {
        return updateProgress(userId, topicId) { it.copy(lessonCompleted = true) }
    }

    override suspend fun updateQuizScore(userId: String, topicId: String, score: Float): Result<Unit> {
        return updateProgress(userId, topicId) { it.copy(quizScore = score) }
    }

    override suspend fun markVisualizationWatched(userId: String, topicId: String): Result<Unit> {
        return updateProgress(userId, topicId) { it.copy(visualizationWatched = true) }
    }

    private suspend fun updateProgress(
        userId: String,
        topicId: String,
        updateBlock: (ProgressEntity) -> ProgressEntity
    ): Result<Unit> {
        return try {
            val existing = progressDao.getProgress(userId, topicId) ?: ProgressEntity(
                id = "${userId}_${topicId}",
                userId = userId,
                topicId = topicId,
                lessonCompleted = false,
                visualizationWatched = false,
                problemsSolved = 0,
                quizScore = null,
                score = 0
            )
            val updated = updateBlock(existing)
            progressDao.upsert(updated)
            
            // Sync to Supabase
            supabaseClient.postgrest["progress"].upsert(updated)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun ProgressEntity.toDomain() = Progress(
        id = id,
        userId = userId,
        topicId = topicId,
        lessonCompleted = lessonCompleted,
        visualizationWatched = visualizationWatched,
        problemsSolved = problemsSolved,
        quizScore = quizScore,
        score = score
    )
}