package com.squareup.android.api.directory

import com.squareup.android.api.directory.data.GetEmployeesResponseDto
import retrofit2.http.GET

interface DirectoryApi {
    @GET("/sq-mobile-interview/employees.json")
    suspend fun getEmployees(): GetEmployeesResponseDto
}