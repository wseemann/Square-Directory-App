package com.squareup.android.api.directory.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetEmployeesResponseDto(
    @Json(name = "employees")
    val employees: List<EmployeeDto> = listOf()
)