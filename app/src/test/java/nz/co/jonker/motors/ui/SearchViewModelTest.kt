package nz.co.jonker.motors.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
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

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val mock1 = MockVehicleData(1)
    private val mock2 = MockVehicleData(2)

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { repo.search(any(), any(), any()) } returns listOf(mock1.dto, mock2.dto)
        viewModel = SearchViewModel(repo)
    }

    @After
    fun tearDown() {
        confirmVerified(repo)
    }

    @Test
    fun `When repo returns an error Then screen state should be Error`() = runTest {
        coEvery { repo.search(any(), any(), any()) } throws  IllegalArgumentException("Test exception")

        viewModel.search("make", "model", "year")

        assertEquals(SearchViewModel.ScreenState.Error("Test exception"), viewModel.screenStateLiveData.value)
        coVerify { repo.search("make", "model", "year") }
    }

    @Test
    fun `When search results positive Then screen state should be Positive`() = runTest {
        viewModel.search("make", "model", "year")

        assertEquals(SearchViewModel.ScreenState.Good, viewModel.screenStateLiveData.value)
        coVerify { repo.search("make", "model", "year") }
    }

    @Test
    fun `When search results not returned Then screen state should be Loading`() {
        val scheduler = TestCoroutineScheduler()
        Dispatchers.setMain(StandardTestDispatcher(scheduler))
        runTest {
            coEvery { repo.search(any(), any(), any()) } returns listOf()

            viewModel.search("make", "model", "year")

            assertEquals(SearchViewModel.ScreenState.Loading, viewModel.screenStateLiveData.value)
            scheduler.runCurrent()

            coVerify { repo.search("make", "model", "year") }
        }
    }

    @Test
    fun `When search results loading Then update searchResult LiveData`() = runTest {
        viewModel.search("make", "model", "year")

        assertEquals(SearchViewModel.ScreenState.Good, viewModel.screenStateLiveData.value)
        coVerify { repo.search("make", "model", "year") }
    }

    @Test
    fun `When calling search Then update searchResult LiveData`() = runTest {
        val expected = listOf(mock1.presentationItem, mock2.presentationItem)

        viewModel.search("make", "model", "year")

        assertEquals(expected, viewModel.searchLiveData.value)
        coVerify { repo.search("make", "model", "year") }
    }
}

class MockVehicleData(index: Int) {

    val dto: VehicleDto = VehicleDto(
        "id$index",
        "name$index",
        "title$index",
        "make$index",
        "model$index",
        "year$index",
        "price$index"
    )

    val presentationItem = VehiclePresentationItem(
        "id$index",
        "name$index",
        "title$index",
        "year$index",
        "price$index"
    )
}
