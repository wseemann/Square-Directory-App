package com.squareup.android.api

import com.squareup.android.api.directory.DirectoryApi
import com.squareup.android.api.directory.DirectoryRepository
import com.squareup.android.api.directory.DirectoryRepositoryImpl
import com.squareup.android.api.directory.data.GetEmployeesResponseDto
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
class DirectoryRepositoryImplTest {

    private lateinit var dispatcher: CoroutineDispatcher

    private lateinit var onError: suspend (errorMessage: String) -> Unit
    @MockK
    private lateinit var directoryApi: DirectoryApi
    
    @Before
    fun setup() {
        MockKAnnotations.init(this)

        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)

        onError = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `test getEmployees with a successful API response`() = runTest {
        val getEmployeesResponseDto = GetEmployeesResponseDto(listOf())
        coEvery { directoryApi.getEmployees() } returns getEmployeesResponseDto

        val response = getDirectoryRepository().getEmployees(
            fromCache = false,
            onError = onError
        ).firstOrNull()

        assertEquals(getEmployeesResponseDto, response)
        coVerify(exactly = 0) { onError.invoke(any()) }
        coVerify(exactly = 1) { directoryApi.getEmployees() }
        confirmVerified(directoryApi)
    }

    @Test
    fun `test getEmployees with a failed API request`() = runTest {
        coEvery { directoryApi.getEmployees() } throws Exception()

        val response = getDirectoryRepository().getEmployees(
            fromCache = false,
            onError = onError
        ).firstOrNull()

        assertNull(response)
        coVerify(exactly = 1) { onError.invoke(any()) }
        coVerify(exactly = 1) { directoryApi.getEmployees() }
        confirmVerified(directoryApi)
    }

   @Test
    fun `test getEmployees with cache and data is cached`() = runTest {
        val cachedGetEmployeesResponseDto = GetEmployeesResponseDto(listOf())
        val getEmployeesResponseDto = GetEmployeesResponseDto(listOf())
        coEvery { directoryApi.getEmployees() } returns getEmployeesResponseDto

        val response = getDirectoryRepository(cachedGetEmployeesResponseDto).getEmployees(
            fromCache = true,
            onError = onError,
        ).firstOrNull()

        assertEquals(cachedGetEmployeesResponseDto, response)
        coVerify(exactly = 0) { onError.invoke(any()) }
        coVerify(exactly = 0) { directoryApi.getEmployees() }
        confirmVerified(directoryApi)
    }

    @Test
    fun `test getEmployees with cache and data is not cached`() = runTest {
        val getEmployeesResponseDto = GetEmployeesResponseDto(listOf())
        coEvery { directoryApi.getEmployees() } returns getEmployeesResponseDto

        val response = getDirectoryRepository().getEmployees(
            fromCache = true,
            onError = onError,
        ).firstOrNull()

        assertEquals(response, response)
        coVerify(exactly = 0) { onError.invoke(any()) }
        coVerify(exactly = 1) { directoryApi.getEmployees() }
        confirmVerified(directoryApi)
    }

    private fun getDirectoryRepository(
        cachedGetEmployeesResponseDto: GetEmployeesResponseDto? = null
    ): DirectoryRepository {
        return DirectoryRepositoryImpl(
            dispatcher = dispatcher,
            directoryApi = directoryApi,
            cachedGetEmployeesResponseDto
        )
    }
}