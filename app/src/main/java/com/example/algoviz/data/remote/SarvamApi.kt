package com.example.algoviz.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

@Serializable
data class ChatRequest(
    val model: String = "sarvam-m",
    val messages: List<ChatMessage>,
    val language: String = "en-IN"
)

@Serializable
data class ChatMessage(
    val role: String,
    val content: String
)

@Serializable
data class ChatResponse(
    val message: ChatMessage
)

@Serializable
data class TranslationRequest(
    val input: String,
    val source_language: String,
    val target_language: String
)

@Serializable
data class TranslationResponse(
    val translated_text: String
)

interface SarvamApi {
    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") apiKey: String,
        @Body request: ChatRequest
    ): ChatResponse

    @POST("translate")
    suspend fun translateText(
        @Header("Authorization") apiKey: String,
        @Body request: TranslationRequest
    ): TranslationResponse
}