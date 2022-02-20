package nz.co.jonker.motors.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import nz.co.jonker.motors.R
import nz.co.jonker.motors.databinding.ActivitySearchBinding

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModels()

    private val adapter = SearchResultsAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.searchResultsRecycler.adapter = adapter
        binding.searchResultsRecycler.layoutManager = LinearLayoutManager(this)

        // Observe livedata and update a recyclerview of vehicles
        binding.searchButton.setOnClickListener {

            supportFragmentManager.let {
                SearchBottomSheet().apply {
                    show(it, tag)
                }
            }
        }

        viewModel.searchLiveData.observe(this) { items ->
            Log.i("TAG", items.toString())
            adapter.data = items
            adapter.notifyDataSetChanged()
        }
        observeScreenState()
    }

    private fun observeScreenState() {
        viewModel.screenStateLiveData.observe(this) { state ->
            state.setProgressVisibility(binding.progressBar)
            if (state is SearchViewModel.ScreenState.Error) {
                AlertDialog.Builder(this).setTitle("Error")
                    .setMessage(state.message)
                    .show()
            }
        }
    }
}

class SearchResultsAdapter(var data: List<VehiclePresentationItem>) :
    RecyclerView.Adapter<SearchResultHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultHolder =
        SearchResultHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        )

    override fun onBindViewHolder(holder: SearchResultHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}

class SearchResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: VehiclePresentationItem) {
        val vehicleTitle = itemView.findViewById<TextView>(R.id.vehicle_title)
        val vehicleName = itemView.findViewById<TextView>(R.id.vehicle_name)
        val vehicleYear = itemView.findViewById<TextView>(R.id.vehicle_year)
        val vehiclePrice = itemView.findViewById<TextView>(R.id.vehicle_price)
        vehicleTitle.text = item.title
        vehicleName.text = item.name
        vehicleYear.text = item.year
        vehiclePrice.text = item.price
    }
}
