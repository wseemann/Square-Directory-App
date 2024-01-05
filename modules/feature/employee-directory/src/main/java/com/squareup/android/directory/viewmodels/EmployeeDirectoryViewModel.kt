package com.squareup.android.directory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.android.api.directory.DirectoryRepository
import com.squareup.android.directory.model.Employee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeDirectoryViewModel @Inject constructor(
    private val directoryRepository: DirectoryRepository
) : ViewModel() {

    private var cachedEmployees: List<Employee>? = null

    private val _employeeUpdates by lazy {
        MutableSharedFlow<List<Employee>>()
    }
    val employeeUpdates: SharedFlow<List<Employee>> = _employeeUpdates

    fun getEmployeeData(fromCache: Boolean) {
        viewModelScope.launch {
            if (fromCache) {
                cachedEmployees?.let { employees ->
                    _employeeUpdates.emit(employees)
                } ?: run {
                    fetchEmployeeDataFromServer()
                }
            } else {
                fetchEmployeeDataFromServer()
            }
        }
    }

    private suspend fun fetchEmployeeDataFromServer() {
        directoryRepository.getEmployees().collect { response ->
            val employees = response.employees.map { employee ->
                Employee(
                    uuid = employee.uuid.orEmpty(),
                    fullName = employee.fullName.orEmpty(),
                    photoUrlSmall = employee.photoUrlSmall.orEmpty(),
                    team = employee.team.orEmpty()
                )
            }
            cachedEmployees = employees
            _employeeUpdates.emit(employees)
        }
    }
}