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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DirectoryRepositoryImplTest {

    private lateinit var dispatcher: CoroutineDispatcher

    @MockK
    private lateinit var directoryApi: DirectoryApi

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
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
            fromCache = false
        ).firstOrNull()

        assertEquals(getEmployeesResponseDto, response)
        coVerify(exactly = 1) { directoryApi.getEmployees() }
        confirmVerified(directoryApi)
    }

    @Test
    fun `test getEmployees with a failed API request`() = runTest {
        var exception: Exception? = null

        coEvery { directoryApi.getEmployees() } throws Exception()

        val response = try {
            getDirectoryRepository().getEmployees(
                fromCache = false
            ).firstOrNull()
        } catch (ex: Exception) {
            exception = ex
        }

        assertNotNull(exception)
        assertEquals(Unit, response)
        coVerify(exactly = 1) { directoryApi.getEmployees() }
        confirmVerified(directoryApi)
    }

   @Test
    fun `test getEmployees with cache and data is cached`() = runTest {
        val cachedGetEmployeesResponseDto = GetEmployeesResponseDto(listOf())
        val getEmployeesResponseDto = GetEmployeesResponseDto(listOf())
        coEvery { directoryApi.getEmployees() } returns getEmployeesResponseDto

        val response = getDirectoryRepository(cachedGetEmployeesResponseDto).getEmployees(
            fromCache = true
        ).firstOrNull()

        assertEquals(cachedGetEmployeesResponseDto, response)
        coVerify(exactly = 0) { directoryApi.getEmployees() }
        confirmVerified(directoryApi)
    }

    @Test
    fun `test getEmployees with cache and data is not cached`() = runTest {
        val getEmployeesResponseDto = GetEmployeesResponseDto(listOf())
        coEvery { directoryApi.getEmployees() } returns getEmployeesResponseDto

        val response = getDirectoryRepository().getEmployees(
            fromCache = true
        ).firstOrNull()

        assertEquals(response, response)
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