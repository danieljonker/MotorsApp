package nz.co.jonker.motors

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.co.jonker.motors.SearchViewModel.ScreenState.Good
import nz.co.jonker.motors.data.SearchRepo
import nz.co.jonker.motors.data.VehicleDto
import nz.co.jonker.motors.databinding.ActivitySearchBinding
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Observe livedata and update a recyclerview of vehicles
        // Search UI should probably be an overlay like a bottom sheet
        // the makes, models & years should come from a backend, so they'll also come from the viewmodel and be in a selector
        // The model can only be selected after the make, and should be cleared if the make is cleared

        binding.searchButton.setOnClickListener {

            supportFragmentManager.let {
                SearchBottomSheet().apply {
                    show(it, tag)
                }
            }
        }

        viewModel.searchLiveData.observe(this) { items ->
            Log.i("TAG", items.toString())
        }
        observeScreenState()
    }

    private fun observeScreenState() {
        viewModel.screenStateLiveData.observe(this) { state ->
            when(state) {
                Good -> {
                    binding.progressBar.visibility = View.GONE
                }
                is SearchViewModel.ScreenState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).setTitle("Error")
                        .setMessage(state.message)
                        .show()
                }
                SearchViewModel.ScreenState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepo: SearchRepo) : ViewModel() {

    private val _searchResultsLiveData: MutableLiveData<List<VehicleDto>> = MutableLiveData()
    val searchLiveData = _searchResultsLiveData
    private val _screenStateLiveData: MutableLiveData<ScreenState> = MutableLiveData()
    val screenStateLiveData = _screenStateLiveData

    fun search(make: String, model: String, year: String) {
        _screenStateLiveData.postValue(ScreenState.Loading)
        viewModelScope.launch {
            try {
                _searchResultsLiveData.postValue(searchRepo.search(make, model, year))
                _screenStateLiveData.postValue(Good)
            } catch (e: Exception) {
                _screenStateLiveData.postValue(ScreenState.Error(e.message.orEmpty()))
            }
        }
    }

    sealed interface ScreenState {
        object Loading : ScreenState
        class Error(val message: String) : ScreenState
        object Good : ScreenState
    }
}