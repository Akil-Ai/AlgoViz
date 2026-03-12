package com.example.algoviz.data.repository

import com.example.algoviz.domain.model.Problem
import com.example.algoviz.domain.repository.ProblemRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ProblemRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : ProblemRepository {

    private val _problems = MutableStateFlow<List<Problem>>(emptyList())

    override fun getProblems(): Flow<List<Problem>> = _problems.asStateFlow()

    override suspend fun getProblemById(id: String): Problem? {
        return try {
            supabaseClient.postgrest["problems"]
                .select {
                    filter { eq("id", id) }
                }
                .decodeSingle<Problem>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun refreshProblems() {
        try {
            val remoteProblems = supabaseClient.postgrest["problems"]
                .select()
                .decodeList<Problem>()
            _problems.value = remoteProblems
        } catch (_: Exception) {}
    }

    override suspend fun getDailyChallenge(): Problem? {
        return try {
            supabaseClient.postgrest["problems"]
                .select {
                    filter { eq("is_daily_challenge", true) }
                }
                .decodeSingle<Problem>()
        } catch (e: Exception) {
            null
        }
    }
}