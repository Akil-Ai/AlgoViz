package com.example.algoviz.domain.repository

import com.example.algoviz.domain.model.Problem
import kotlinx.coroutines.flow.Flow

interface ProblemRepository {
    fun getProblems(): Flow<List<Problem>>
    suspend fun getProblemById(id: String): Problem?
    suspend fun refreshProblems()
    suspend fun getDailyChallenge(): Problem?
}