package com.squareup.android.api.directory

import android.util.Log
import com.squareup.android.api.directory.data.GetEmployeesResponseDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class DirectoryRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val directoryApi: DirectoryApi
) : DirectoryRepository {
    override fun getEmployees(): Flow<GetEmployeesResponseDto> {
        return flow {
            val s = directoryApi.getEmployees()

            Log.d("------>", s.employees?.size.toString())
            emit(GetEmployeesResponseDto())
        }.flowOn(dispatcher)
    }
}
