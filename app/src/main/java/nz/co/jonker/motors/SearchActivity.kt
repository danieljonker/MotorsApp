package nz.co.jonker.motors

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonClass
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.co.jonker.motors.databinding.ActivitySearchBinding
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // todo: When clicking search, pass values to viewModel
        // Observe livedata and update a recyclerview of vehicles
        // Search UI should probably be an overlay like a bottom sheet
        // the makes, models & years should come from a backend, so they'll also come from the viewmodel and being in a selector
        // The model can only be selected after the make, and should be cleared if the make is cleared

        binding.submitButton.setOnClickListener {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
            viewModel.search(
                binding.make.text.toString(),
                binding.model.text.toString(),
                binding.year.text.toString()
            )
        }

        viewModel.searchLiveData.observe(this) { items ->
            Log.i("TAG", items.toString())
        }
    }
}

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepo: SearchRepo) : ViewModel() {

    private val _searchResultsLiveData: MutableLiveData<List<VehicleDto>> = MutableLiveData()
    val searchLiveData = _searchResultsLiveData

    fun search(make: String, model: String, year: String) {
        viewModelScope.launch {
            _searchResultsLiveData.postValue(searchRepo.search(make, model, year))
        }
    }
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
@JsonClass(generateAdapter = true)
data class SearchResultsDto(val searchResults: List<VehicleDto>)

@JsonClass(generateAdapter = true)
data class VehicleDto(
    val id: String,
    val name: String,
    val title: String,
    val make: String,
    val model: String,
    val year: String,
    val price: String
)