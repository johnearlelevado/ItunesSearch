package com.example.challenge.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.challenge.R
import com.example.challenge.api.common.ApiResponse
import com.example.challenge.api.common.Status
import com.example.challenge.api.itunes.dto.SearchResultModel
import com.example.challenge.databinding.ActivityMainBinding
import com.example.challenge.ui.adapters.SearchResultRVAdapter
import com.example.challenge.util.NetworkStatusUtil
import com.example.challenge.util.SharedPrefsUtils
import com.example.challenge.viewmodels.ItunesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    private val itunesViewModel: ItunesViewModel by viewModels()
    private lateinit var searchResultRVAdapter: SearchResultRVAdapter
    private lateinit var networkStatusUtil: NetworkStatusUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeRecyclerView()
        initializeSearchView()
        initNetworkConnectionStatusHandler()
        initializeViewModel()
        saveLastVisit()
    }

    /**
     * initialize handling for observable/flowable data from Network and DB
     * */
    private fun initializeViewModel() {
        // handle network response
        itunesViewModel.itunesItemsObservable.observe(this, Observer {
            handleResponse(it)
        })
        // update the adapter with new data inserted into DB
        itunesViewModel.repository.getItunesItemsResults().subscribe { result ->
            lifecycleScope.launch {
                binding.swipeRefresh.isRefreshing = false
                searchResultRVAdapter.updateResults(result)
            }
        }
    }

    /**
     *  initialize the SearchView with data from SharedPreferences and set listeners for text changes and submission
     * */
    private fun initializeSearchView() {
        binding.input.setQuery(SharedPrefsUtils.getStringPreference(this, LAST_SEARCH_TERM), false)
        binding.input.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                itunesViewModel.getItunesItems("$query", ENTITY, COUNTRY)
                SharedPrefsUtils.setStringPreference(this@MainActivity, LAST_SEARCH_TERM, "$query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    itunesViewModel.getItunesItems("", ENTITY, COUNTRY)
                    SharedPrefsUtils.setStringPreference(this@MainActivity, LAST_SEARCH_TERM, "")
                }
                return false
            }
        })
    }

    /**
     * Set RecyclerView LayoutManager and Adapter
     * Set callback when item is clicked
     * Set List Refresh handler
     * */
    private fun initializeRecyclerView() {
        // Search result recycler view
        val layoutManager: StaggeredGridLayoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                StaggeredGridLayoutManager(SPAN_COUNT_PORT, StaggeredGridLayoutManager.VERTICAL)
            } else {
                StaggeredGridLayoutManager(SPAN_COUNT_LAND, StaggeredGridLayoutManager.VERTICAL)
            }
        searchResultRVAdapter = SearchResultRVAdapter(ResultClickListener { result ->
            AlertDialog.Builder(this)
                .setTitle(result?.trackName)
                .setMessage(result?.longDescription)
                .setCancelable(true)
                .create()
                .show()
        })
        binding.list.layoutManager = layoutManager
        binding.list.adapter = searchResultRVAdapter
        binding.swipeRefresh.setOnRefreshListener {
            if (!binding.input.query.isNullOrEmpty()) {
                itunesViewModel.getItunesItems("${binding.input.query}", ENTITY, COUNTRY)
            } else {
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    /**
     *  Save the Date/Time from the last opening of the app
     *  Show the last visit data if any stored on SharedPreferences
     * */
    private fun saveLastVisit() {
        SharedPrefsUtils.getStringPreference(this, LAST_VISIT)?.let {
            binding.tvLastVisit.visibility = View.VISIBLE
            binding.tvLastVisit.text = getString(R.string.last_visit, it)
        }
        val date = SimpleDateFormat(DATE_FORMAT).format(Date()).toString()
        SharedPrefsUtils.setStringPreference(this, LAST_VISIT, date)
    }

    /**
     * Handle no network scenario.
     * Show an 'offline' message when the network is not available and
     * retrieve retry the API request when the network is available
     * */
    private fun initNetworkConnectionStatusHandler() {
        networkStatusUtil = NetworkStatusUtil(this) {
            itunesViewModel.getItunesItems("${binding.input.query}",ENTITY, COUNTRY)
        }.apply {
            build(binding.tvNetworkStatusBar)
        }
    }


    /**
     * handle response from livedata
     * */
    private fun handleResponse(apiResponse: ApiResponse<SearchResultModel>?) {
        apiResponse?.let {
            when(it.mStatus) {
                Status.SUCCESS -> {
                    binding.swipeRefresh.isRefreshing = false
                }
                Status.ERROR, Status.FAIL -> {
                    binding.swipeRefresh.isRefreshing = false
                    showToast("Something went wrong...")
                }
            }
        }
    }


    companion object {
        const val SPAN_COUNT_PORT = 3
        const val SPAN_COUNT_LAND = 4
        const val LAST_SEARCH_TERM = "last_search_term"
        const val LAST_VISIT = "last_visit"
        const val ENTITY = "movie"
        const val COUNTRY = "au"
        const val DATE_FORMAT = "yyyy-MM-dd hh:mm:ss"
    }
}