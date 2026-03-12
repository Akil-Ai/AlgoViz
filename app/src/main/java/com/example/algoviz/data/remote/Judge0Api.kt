package com.example.algoviz.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
data class SubmissionRequest(
    val source_code: String,
    val language_id: Int,
    val stdin: String? = null,
    val expected_output: String? = null
)

@Serializable
data class SubmissionResponse(
    val token: String
)

@Serializable
data class SubmissionResult(
    val stdout: String? = null,
    val time: String? = null,
    val memory: Int? = null,
    val stderr: String? = null,
    val token: String,
    val compile_output: String? = null,
    val message: String? = null,
    val status: Status
)

@Serializable
data class Status(
    val id: Int,
    val description: String
)

interface Judge0Api {
    @POST("submissions")
    suspend fun createSubmission(
        @Body request: SubmissionRequest,
        @Query("base64_encoded") base64: Boolean = false,
        @Query("wait") wait: Boolean = true
    ): SubmissionResult

    @GET("submissions/{token}")
    suspend fun getSubmission(
        @Path("token") token: String,
        @Query("base64_encoded") base64: Boolean = false
    ): SubmissionResult
}