package nz.co.jonker.motors

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import nz.co.jonker.motors.databinding.ActivitySearchBinding
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_search)

        // todo: When clicking search, pass values to viewModel
        // Observe livedata and update a recyclerview of vehicles
        // Search UI should probably be an overlay like a bottom sheet
        // the makes, models & years should come from a backend, so they'll also come from the viewmodel and being in a selector
        // The model can only be selected after the make, and should be cleared if the make is cleared

        binding.submitButton.setOnClickListener {

        }
    }
}

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepo: SearchRepo) : ViewModel() {


}

class SearchRepoImpl @Inject constructor(private val service: SearchService) : SearchRepo {
    override suspend fun search(make: String, model: String, year: String): List<VehicleDto> {
        return service.search(make, model, year).searchResults
    }
}

interface SearchRepo {
    suspend fun search(make: String, model: String, year: String): List<VehicleDto>
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