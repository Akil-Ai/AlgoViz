package com.example.algoviz.domain.repository

import com.example.algoviz.data.remote.ChatMessage
import kotlinx.coroutines.flow.Flow

interface AIRepository {
    suspend fun getAIResponse(messages: List<ChatMessage>, language: String): Result<String>
    suspend fun translate(text: String, targetLanguage: String): Result<String>
    fun getChatHistory(topicId: String): Flow<List<ChatMessage>>
}