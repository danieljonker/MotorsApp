package nz.co.jonker.motors.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.co.jonker.motors.data.SearchRepo
import nz.co.jonker.motors.data.VehicleDto
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepo: SearchRepo) : ViewModel() {

    private val _searchResultsLiveData: MutableLiveData<List<VehiclePresentationItem>> = MutableLiveData()
    val searchLiveData = _searchResultsLiveData
    private val _screenStateLiveData: MutableLiveData<ScreenState> = MutableLiveData()
    val screenStateLiveData = _screenStateLiveData

    fun search(make: String, model: String, year: String) {
        _screenStateLiveData.postValue(ScreenState.Loading)
        viewModelScope.launch {
            try {
                val items = searchRepo.search(make, model, year).map { VehiclePresentationItem.fromDto(it) }
                _searchResultsLiveData.postValue(items)
                _screenStateLiveData.postValue(ScreenState.Good)
            } catch (e: Exception) {
                _screenStateLiveData.postValue(ScreenState.Error(e.message.orEmpty()))
            }
        }
    }

    sealed interface ScreenState {
        fun setProgressVisibility(progressBar: View)

        object Loading : ScreenState {
            override fun setProgressVisibility(progressBar: View) {
                progressBar.visibility = View.VISIBLE
            }
        }
        data class Error(val message: String) : ScreenState {
            override fun setProgressVisibility(progressBar: View) {
                progressBar.visibility = View.GONE
            }
        }
        object Good : ScreenState {
            override fun setProgressVisibility(progressBar: View) {
                progressBar.visibility = View.GONE
            }
        }
    }
}

data class VehiclePresentationItem(
    val id: String,
    val name: String,
    val title: String,
    val year: String,
    val price: String
) {
    companion object {
        fun fromDto(dto: VehicleDto): VehiclePresentationItem = with(dto) {
            VehiclePresentationItem(id, name, title, year, price)
        }
    }
}
