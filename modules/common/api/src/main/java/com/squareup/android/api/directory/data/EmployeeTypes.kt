package com.squareup.android.api.directory.data

import com.squareup.moshi.ToJson

enum class EmployeeTypes(val type: String) {
    FULL_TIME("FULL_TIME"),
    PART_TIME("PART_TIME"),
    CONTRACTOR("CONTRACTOR")
}

internal class EmployeeTypesAdapter {
    @ToJson
    fun toJson(deviceEventsRequestTypes: EmployeeTypes): String = deviceEventsRequestTypes.type
}
