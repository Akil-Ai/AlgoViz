package com.example.algoviz.di

import com.example.algoviz.data.repository.AIRepositoryImpl
import com.example.algoviz.data.repository.AuthRepositoryImpl
import com.example.algoviz.data.repository.ProblemRepositoryImpl
import com.example.algoviz.data.repository.ProgressRepositoryImpl
import com.example.algoviz.data.repository.SubmissionRepositoryImpl
import com.example.algoviz.data.repository.TopicRepositoryImpl
import com.example.algoviz.data.repository.UserRepositoryImpl
import com.example.algoviz.domain.repository.AIRepository
import com.example.algoviz.domain.repository.AuthRepository
import com.example.algoviz.domain.repository.ProblemRepository
import com.example.algoviz.domain.repository.ProgressRepository
import com.example.algoviz.domain.repository.SubmissionRepository
import com.example.algoviz.domain.repository.TopicRepository
import com.example.algoviz.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindTopicRepository(impl: TopicRepositoryImpl): TopicRepository

    @Binds
    @Singleton
    abstract fun bindProblemRepository(impl: ProblemRepositoryImpl): ProblemRepository

    @Binds
    @Singleton
    abstract fun bindSubmissionRepository(impl: SubmissionRepositoryImpl): SubmissionRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(impl: ProgressRepositoryImpl): ProgressRepository

    @Binds
    @Singleton
    abstract fun bindAIRepository(impl: AIRepositoryImpl): AIRepository
}