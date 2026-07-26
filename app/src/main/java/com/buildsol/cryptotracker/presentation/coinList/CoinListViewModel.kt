package com.buildsol.cryptotracker.presentation.coinList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildsol.cryptotracker.domain.repository.CoinRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.time.Duration.Companion.milliseconds

class CoinListViewModel(
    private val repository: CoinRepository
) : ViewModel() {

    private val _listState = MutableStateFlow<CoinListUiState>(CoinListUiState.Loading)
    val listState: StateFlow<CoinListUiState> = _listState.asStateFlow()
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    private var currentPage = 1
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val _searchState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadNextPage()
        observeSearchQuery()
        try{ viewModelScope.launch { repository.loadCoinIndex() } }catch (e: Exception){
            Log.e("DEBUG","this is the error this might be the reason of crash")
        }
    }

    fun loadNextPage() {
        if (loadJob?.isActive == true) return

        val current = _listState.value
        val isInitialLoad = current !is CoinListUiState.Success

        loadJob = viewModelScope.launch {
            _listState.value = when {
                isInitialLoad -> CoinListUiState.Loading
                else -> current.copy(isLoadingMore = true)
            }

            try {
                val newCoins = repository.getMarket(page = currentPage)
                val existingCoins = (current as? CoinListUiState.Success)?.coins.orEmpty()
                val combined = existingCoins + newCoins

                _listState.value = if (combined.isEmpty()) {
                    CoinListUiState.Empty
                } else {
                    CoinListUiState.Success(coins = combined, isLoadingMore = false)
                }
                currentPage++
            } catch (e: IOException) {
                _listState.value = restoreOrError(current, "Check your connection and try again.")
            } catch (e: Exception) {
                _listState.value = restoreOrError(current, "Something went wrong. Please try again.")
            }
        }
    }

    private fun restoreOrError(current: CoinListUiState, message: String): CoinListUiState {
        return if (current is CoinListUiState.Success) {
            current.copy(isLoadingMore = false)
        } else {
            CoinListUiState.Error(message)
        }
    }

    fun retry() {
        loadNextPage()
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            currentPage = 1
            try {
                val coins = repository.getMarket(page = 1)
                _listState.value = if (coins.isEmpty()) {
                    CoinListUiState.Empty
                } else {
                    CoinListUiState.Success(coins = coins, isLoadingMore = false)
                }
                currentPage = 2
            } catch (e: IOException) {
                if (_listState.value !is CoinListUiState.Success) {
                    _listState.value = CoinListUiState.Error("Check your connection and try again.")
                }
            } catch (e: Exception) {
                if (_listState.value !is CoinListUiState.Success) {
                    _listState.value = CoinListUiState.Error("Something went wrong. Please try again.")
                }
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    val autocompleteSuggestions = searchQuery
        .map { query -> if (query.isBlank()) emptyList() else repository.autocomplete(query) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchState.value = SearchUiState.Idle
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        searchQuery
            .debounce(300.milliseconds)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    _searchState.value = SearchUiState.Idle
                } else {
                    performSearch(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun performSearch(query: String) {
        _searchState.value = SearchUiState.Loading
        try {
            val results = repository.search(query)
            _searchState.value = if (results.isEmpty()) {
                SearchUiState.Empty
            } else {
                SearchUiState.Success(results)
            }
        } catch (e: IOException) {
            _searchState.value = SearchUiState.Error("Check your connection and try again.")
        } catch (e: Exception) {
            _searchState.value = SearchUiState.Error("Something went wrong. Please try again.")
        }
    }
}