package com.squareup.android.directory.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.android.api.directory.DirectoryRepository
import com.squareup.android.directory.model.Employee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeDirectoryViewModel @Inject constructor(
    private val directoryRepository: DirectoryRepository
) : ViewModel() {

    private val _employeeUpdates by lazy {
        MutableSharedFlow<List<Employee>>()
    }
    val employeeUpdates: SharedFlow<List<Employee>> = _employeeUpdates

    fun getEmployees() {
        viewModelScope.launch {
            directoryRepository.getEmployees().collect { response ->
                val employees = response.employees.map { employee ->
                    Employee(
                        uuid = employee.uuid.orEmpty(),
                        fullName = employee.fullName.orEmpty(),
                        photoUrlSmall = employee.photoUrlSmall.orEmpty(),
                        team = employee.team.orEmpty()
                    )
                }
                _employeeUpdates.emit(employees)
            }
        }
    }
}