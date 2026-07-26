package com.buildsol.cryptotracker.presentation.coinList

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

    // ---------- Paginated market list ----------
    private val _listState = MutableStateFlow<CoinListUiState>(CoinListUiState.Loading)
    val listState: StateFlow<CoinListUiState> = _listState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var currentPage = 1

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    // Guards against duplicate concurrent page loads from a fast-scrolling
    // LazyColumn firing loadNextPage() multiple times before the first
    // request returns. Without this, quick scrolling can trigger 3-4
    // overlapping network calls and append duplicate/out-of-order coins.
    private var loadJob: Job? = null

    init {
        loadNextPage()
        observeSearchQuery()
        // Fire-and-forget: builds the in-memory autocomplete index once.
        // Autocomplete simply returns empty results until this completes —
        // acceptable since it's near-instant and non-blocking for the rest
        // of the screen.
        viewModelScope.launch { repository.loadCoinIndex() }
    }

    fun loadNextPage() {
        if (loadJob?.isActive == true) return

        val current = _listState.value
        val isInitialLoad = current !is CoinListUiState.Success

        loadJob = viewModelScope.launch {
            _listState.value = when {
                isInitialLoad -> CoinListUiState.Loading
                current is CoinListUiState.Success -> current.copy(isLoadingMore = true)
                else -> current
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

                // Only advance the page pointer on success — this is what
                // makes retry() just work by re-calling loadNextPage().
                currentPage++
            } catch (e: IOException) {
                _listState.value = restoreOrError(current, "Check your connection and try again.")
            } catch (e: Exception) {
                _listState.value = restoreOrError(current, "Something went wrong. Please try again.")
            }
        }
    }

    // If we already had a successful page loaded and a *subsequent* page
    // fails, don't blow away the working list into a full-screen Error —
    // that would hide data the user already has. Only show Error when
    // there's nothing on screen yet (initial load failure).
    private fun restoreOrError(current: CoinListUiState, message: String): CoinListUiState {
        return if (current is CoinListUiState.Success) {
            current.copy(isLoadingMore = false)
        } else {
            CoinListUiState.Error(message)
        }
    }

    fun retry() {
        // currentPage never advanced on the failed attempt, so this simply
        // re-requests the same page that just failed.
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
                // Keep prior data visible on a failed refresh rather than
                // wiping a working list into a full-screen error over a
                // transient blip — only show Error if we had nothing before.
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

    // ---------- Search ----------


    // Instant, local, no network cost — purely an in-memory index lookup, so
    // no debounce needed here. This is deliberately separate from the
    // debounced network search below.
    val autocompleteSuggestions = searchQuery
        .map { query -> if (query.isBlank()) emptyList() else repository.autocomplete(query) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchState.value = SearchUiState.Idle
        // listState is untouched — clearing search just means the UI swaps
        // back to whatever paginated state was already sitting there.
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