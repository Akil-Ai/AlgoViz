package com.example.algoviz.data.repository

import com.example.algoviz.data.remote.Judge0Api
import com.example.algoviz.data.remote.SubmissionRequest
import com.example.algoviz.data.remote.SubmissionResult
import com.example.algoviz.domain.model.Submission
import com.example.algoviz.domain.repository.AuthRepository
import com.example.algoviz.domain.repository.SubmissionRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SubmissionRepositoryImpl @Inject constructor(
    private val judge0Api: Judge0Api,
    private val supabaseClient: SupabaseClient,
    private val authRepository: AuthRepository
) : SubmissionRepository {

    override suspend fun submitCode(
        problemId: String,
        code: String,
        languageId: Int
    ): Result<SubmissionResult> {
        return try {
            val result = judge0Api.createSubmission(
                SubmissionRequest(
                    source_code = code,
                    language_id = languageId
                )
            )
            
            // Save to Supabase
            val userId = authRepository.getCurrentUser()?.id ?: ""
            if (userId.isNotEmpty()) {
                supabaseClient.postgrest["submissions"].insert(
                    Submission(
                        userId = userId,
                        problemId = problemId,
                        code = code,
                        language = languageId.toString(),
                        verdict = result.status.description,
                        runtimeMs = result.time?.replace("s", "")?.toFloatOrNull()?.let { (it * 1000).toInt() },
                        memoryKb = result.memory
                    )
                )
            }
            
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSubmissionsByUser(userId: String): Flow<List<Submission>> = flow {
        try {
            val submissions = supabaseClient.postgrest["submissions"]
                .select {
                    filter { eq("user_id", userId) }
                }
                .decodeList<Submission>()
            emit(submissions)
        } catch (_: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getSubmissionById(id: String): Submission? {
        return try {
            supabaseClient.postgrest["submissions"]
                .select {
                    filter { eq("id", id) }
                }
                .decodeSingle<Submission>()
        } catch (e: Exception) {
            null
        }
    }
}