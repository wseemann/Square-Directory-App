package com.squareup.android.api.directory

import kotlinx.coroutines.flow.Flow

interface DirectoryRepositoryImpl {
    fun getEmployees(): Flow<String>
}
