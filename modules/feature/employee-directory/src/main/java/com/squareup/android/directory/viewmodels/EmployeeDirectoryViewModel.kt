package com.squareup.android.directory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.android.api.directory.DirectoryRepository
import com.squareup.android.directory.model.Employee
import com.squareup.android.directory.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeDirectoryViewModel @Inject constructor(
    private val directoryRepository: DirectoryRepository
) : ViewModel() {

    private val _employeeUpdates by lazy {
        MutableSharedFlow<UiState<List<Employee>>>()
    }
    val employeeUpdates: SharedFlow<UiState<List<Employee>>> = _employeeUpdates

    fun getEmployeeData(fromCache: Boolean) {
        viewModelScope.launch {
            directoryRepository.getEmployees(
                fromCache = fromCache
            ).catch {
                _employeeUpdates.emit(UiState.Error(it.message))
            }.collect { response ->
                val employees = response.employees.map { employee ->
                    Employee(
                        uuid = employee.uuid,
                        fullName = employee.fullName,
                        photoUrlSmall = employee.photoUrlSmall,
                        team = employee.team
                    )
                }
                _employeeUpdates.emit(UiState.Success(employees))
            }
        }
    }
}