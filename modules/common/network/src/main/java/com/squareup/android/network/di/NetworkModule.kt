package com.squareup.android.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val DEFAULT_CONNECT_TIMEOUT_MS = 5000L
    private const val DEFAULT_READ_TIMEOUT_MS = 5000L
    private const val DEFAULT_WRITE_TIMEOUT_MS = 5000L

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .build()
    }
}
