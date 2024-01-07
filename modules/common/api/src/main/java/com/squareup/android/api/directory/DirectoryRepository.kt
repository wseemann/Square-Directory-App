package com.squareup.android.api.directory

import com.squareup.android.api.directory.data.GetEmployeesResponseDto
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.Throws

interface DirectoryRepository {
    @Throws(Exception::class)
    fun getEmployees(
        fromCache: Boolean
    ): Flow<GetEmployeesResponseDto>
}
