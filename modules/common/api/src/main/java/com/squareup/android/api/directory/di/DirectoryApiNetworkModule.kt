package com.squareup.android.api.directory.di

import android.content.Context
import com.squareup.android.api.R
import com.squareup.android.api.directory.DirectoryApi
import com.squareup.android.api.directory.data.EmployeeTypesAdapter
import com.squareup.moshi.Moshi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DirectoryApiNetworkModule {
    @Singleton
    @Provides
    internal fun providesDirectoryApi(
        @ApplicationContext context: Context,
        httpClient: OkHttpClient
    ): DirectoryApi {
        val moshi = Moshi.Builder()
            .add(EmployeeTypesAdapter())
            .build()

        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DirectoryApi::class.java)
    }
}