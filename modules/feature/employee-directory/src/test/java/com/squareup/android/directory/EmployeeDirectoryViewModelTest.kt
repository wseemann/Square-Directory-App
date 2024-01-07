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
import io.mockk.slot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class EmployeeDirectoryViewModelTest {

    private lateinit var dispatcher: CoroutineDispatcher

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
        every { directoryRepository.getEmployees(false, any()) } returns flowOf(GetEmployeesResponseDto(listOf()))

        employeeDirectoryViewModel.getEmployeeData(false)

        assertTrue(state.await() is UiState.Success)
        coVerify(exactly = 1) { directoryRepository.getEmployees(fromCache = false, onError = any()) }
        confirmVerified(directoryRepository)
    }

    @Test
    fun `test getEmployeeData failure returns the correct UI state`() = runTest {
        val onErrorSlot = slot<suspend (String) -> Unit>()
        val state = async {
            employeeDirectoryViewModel.employeeUpdates.first()
        }
        every { directoryRepository.getEmployees(false, capture(onErrorSlot)) } returns emptyFlow()

        employeeDirectoryViewModel.getEmployeeData(false)
        delay(2000)
        onErrorSlot.captured.invoke("")

        assertTrue(state.await() is UiState.Error)
    }
}