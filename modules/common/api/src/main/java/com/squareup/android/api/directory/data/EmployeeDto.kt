package com.squareup.android.api.directory.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeDto(
    @Json(name = "uuid")
    val uuid: String? = null,
    @Json(name = "full_name")
    val fullName: String? = null,
    @Json(name = "phone_number")
    val phoneNumber: String? = null,
    @Json(name = "email_address")
    val emailAddress: String? = null,
    @Json(name = "biography")
    val biography: String? = null,
    @Json(name = "photo_url_small")
    val photoUrlSmall: String? = null,
    @Json(name = "photo_url_large")
    val photoUrlLarge: String? = null,
    @Json(name = "team")
    val team: String? = null,
    @Json(name = "employee_type")
    val employeeType: String? = null
)
