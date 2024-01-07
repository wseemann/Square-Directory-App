package com.squareup.android.api.directory

import com.squareup.android.api.directory.data.GetEmployeesResponseDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

internal class DirectoryRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val directoryApi: DirectoryApi
) : DirectoryRepository {
    override fun getEmployees(
        onLoading: suspend () -> Unit,
        onError: suspend (errorMessage: String) -> Unit
    ): Flow<GetEmployeesResponseDto> {
        return flow {
            onLoading()
            emit(directoryApi.getEmployees())
        }.flowOn(dispatcher)
            .catch {
                Timber.e(it,"Failed to retrieve employees from the directory")
                onError(it.message ?: "An unknown error occurred")
            }
    }
}
