package com.squareup.android.api.directory.di

import com.squareup.android.api.directory.DirectoryApi
import com.squareup.android.api.directory.DirectoryRepository
import com.squareup.android.api.directory.DirectoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DirectoryApiModule {
    @Singleton
    @Provides
    fun providesDirectoryRepository(
        directoryApi: DirectoryApi
    ): DirectoryRepository {
        return DirectoryRepositoryImpl(
            Dispatchers.IO,
            directoryApi
        )
    }
}
