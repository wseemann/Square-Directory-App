package com.squareup.android.directory

import com.squareup.android.api.directory.DirectoryRepository
import com.squareup.android.api.directory.data.GetEmployeesResponseDto
import com.squareup.android.directory.model.UiState
import com.squareup.android.directory.viewmodels.EmployeeDirectoryViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.After
import org.junit.Test
import org.junit.Before
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
class EmployeeDirectoryViewModelTest {

    private lateinit var dispatcher: CoroutineDispatcher

    @MockK
    private lateinit var directoryRepository: DirectoryRepository
    private lateinit var employeeDirectoryViewModel: EmployeeDirectoryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)

        employeeDirectoryViewModel = EmployeeDirectoryViewModel(directoryRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `test getEmployeeData success returns the correct UI state`() = runTest {
        val state = async {
            employeeDirectoryViewModel.employeeUpdates.first()
        }
        every { directoryRepository.getEmployees(false) } returns flowOf(GetEmployeesResponseDto(listOf()))

        employeeDirectoryViewModel.getEmployeeData(false)

        assertTrue(state.await() is UiState.Success)
        coVerify(exactly = 1) { directoryRepository.getEmployees(fromCache = false) }
        confirmVerified(directoryRepository)
    }

    @Test
    fun `test getEmployeeData failure returns the correct UI state`() = runTest {
        val state = async {
            employeeDirectoryViewModel.employeeUpdates.first()
        }
        every { directoryRepository.getEmployees(false) } returns flow {
            throw Exception()
        }

        employeeDirectoryViewModel.getEmployeeData(false)

        assertTrue(state.await() is UiState.Error)
        coVerify(exactly = 1) { directoryRepository.getEmployees(fromCache = false) }
        confirmVerified(directoryRepository)
    }
}