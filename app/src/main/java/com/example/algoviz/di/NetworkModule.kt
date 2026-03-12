package com.example.algoviz.di

import com.example.algoviz.data.remote.Judge0Api
import com.example.algoviz.data.remote.SarvamApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideSarvamApi(okHttpClient: OkHttpClient, json: Json): SarvamApi {
        return Retrofit.Builder()
            .baseUrl("https://api.sarvam.ai/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(SarvamApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJudge0Api(okHttpClient: OkHttpClient, json: Json): Judge0Api {
        return Retrofit.Builder()
            .baseUrl("https://judge0-ce.p.rapidapi.com/") // Example RapidAPI endpoint
            .client(okHttpClient.newBuilder().addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-RapidAPI-Key", "YOUR_RAPIDAPI_KEY")
                    .addHeader("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }.build())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(Judge0Api::class.java)
    }
}