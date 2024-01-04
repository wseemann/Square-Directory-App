package com.squareup.android.api.directory

import com.squareup.android.api.directory.data.GetEmployeesResponseDto
import kotlinx.coroutines.flow.Flow

interface DirectoryRepository {
    fun getEmployees(): Flow<GetEmployeesResponseDto>
}
