package com.squareup.android.api.directory

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class DirectoryRepository(
    dispatcher: CoroutineDispatcher,
    directoryApi: DirectoryApi
) : DirectoryRepositoryImpl {
    override fun getEmployees(): Flow<String> {
        return emptyFlow()
    }
}
