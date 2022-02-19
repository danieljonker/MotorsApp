package nz.co.jonker.motors

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nz.co.jonker.motors.data.SearchRepo
import nz.co.jonker.motors.data.VehicleDto
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private val repo: SearchRepo = mockk(relaxed = true)
    private lateinit var viewModel: SearchViewModel

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val expected = listOf(createVehicle(1), createVehicle(2))

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { repo.search(any(), any(), any()) } returns expected
        viewModel = SearchViewModel(repo)
    }

    @After
    fun tearDown() {
        confirmVerified(repo)
    }

    @Test
    fun `When calling search Then update searchResult LiveData`() = runTest {
        viewModel.search("make", "model", "year")

        assertEquals(expected, viewModel.searchLiveData.value)
        coVerify { repo.search(any(), any(), any()) }
    }
}

private fun createVehicle(index: Int): VehicleDto {
    return VehicleDto(
        "id$index",
        "name$index",
        "title$index",
        "make$index",
        "model$index",
        "year$index",
        "price$index"
    )
}