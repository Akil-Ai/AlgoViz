package com.example.algoviz.domain.repository

import com.example.algoviz.domain.model.Submission
import com.example.algoviz.data.remote.SubmissionResult
import kotlinx.coroutines.flow.Flow

interface SubmissionRepository {
    suspend fun submitCode(
        problemId: String,
        code: String,
        languageId: Int
    ): Result<SubmissionResult>
    
    fun getSubmissionsByUser(userId: String): Flow<List<Submission>>
    suspend fun getSubmissionById(id: String): Submission?
}