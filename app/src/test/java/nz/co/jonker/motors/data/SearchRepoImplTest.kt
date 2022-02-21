package nz.co.jonker.motors.data

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

@ExperimentalCoroutinesApi
class SearchRepoImplTest {

    private val service: SearchService = mockk()
    private val repo = SearchRepoImpl(service)

    @Test(expected = Exception::class)
    fun `Given service erros When Repo called, also return error`() = runTest {
        coEvery { service.search(MAKE, MODEL, YEAR) } throws Exception()

        repo.search(MAKE, MODEL, YEAR)
    }

    @Test
    fun `Given service succeeds When Repo called, return data in new structure`() = runTest {
        val vehicle = VehicleDto("1", "n", "t", "m", "m", "y", "p")
        coEvery { service.search(MAKE, MODEL, YEAR) } returns SearchResultsDto(listOf(vehicle))

        val result = repo.search(MAKE, MODEL, YEAR)

        assertEquals(listOf(vehicle), result)
    }
}

private const val YEAR = "y"
private const val MAKE = "m"
private const val MODEL = "mo"