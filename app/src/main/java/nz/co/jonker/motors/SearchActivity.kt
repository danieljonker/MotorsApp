package nz.co.jonker.motors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        // todo: When clicking search, pass values to viewModel
        // Observe livedata and update a recyclerview of vehicles
        // Search UI should probably be an overlay like a bottom sheet
        // the makes, models & years should come from a backend, so they'll also come from the viewmodel and being in a selector
        // The model can only be selected after the make, and should be cleared if the make is cleared

    }
}

class SearchViewModel : ViewModel() {

}

class SearchRepo {

}

interface SearchService {
    @GET("search")
    suspend fun search(
        @Query("make") make: String,
        @Query("model") model: String,
        @Query("year") year: String
    ): SearchResultsDto
}

data class SearchResultsDto(val searchResults: List<VehicleDto>)

data class VehicleDto(
    val id: String,
    val name: String,
    val title: String,
    val make: String,
    val model: String,
    val year: String,
    val price: BigDecimal
)

private const val BASE_URL = "http://mcuapi.mocklab.io"