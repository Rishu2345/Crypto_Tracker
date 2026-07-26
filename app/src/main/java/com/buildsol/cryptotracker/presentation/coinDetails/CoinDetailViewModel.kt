package com.buildsol.cryptotracker.presentation.coinDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildsol.cryptotracker.domain.repository.CoinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

private const val DEFAULT_CHART_DAYS = 7

class CoinDetailViewModel(
    private val repository: CoinRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Pulled from nav args (e.g. NavHost route "detail/{coinId}"). Koin's
    // viewModel injection picks this up automatically via SavedStateHandle
    // — no manual factory/parametersOf needed for this particular value.
    private val coinId: String = checkNotNull(savedStateHandle["coinId"]) {
        "CoinDetailViewModel requires a coinId nav argument"
    }

    private val _detailState = MutableStateFlow<CoinDetailUiState>(CoinDetailUiState.Loading)
    val detailState: StateFlow<CoinDetailUiState> = _detailState.asStateFlow()

    private val _chartState = MutableStateFlow<ChartUiState>(ChartUiState.Loading)
    val chartState: StateFlow<ChartUiState> = _chartState.asStateFlow()

    init {
        // Two independent launches, not async/awaitAll — awaitAll would
        // force us to wait for both before updating either StateFlow,
        // which defeats the point of "stats render as soon as they're
        // ready, chart renders separately whenever it's ready."
        loadDetails()
        loadChart()
    }

    fun loadDetails() {
        viewModelScope.launch {
            _detailState.value = CoinDetailUiState.Loading
            try {
                val details = repository.getCoinDetails(coinId)
                _detailState.value = CoinDetailUiState.Success(details)
            } catch (e: IOException) {
                _detailState.value = CoinDetailUiState.Error("Check your connection and try again.")
            } catch (e: Exception) {
                _detailState.value = CoinDetailUiState.Error("Something went wrong. Please try again.")
            }
        }
    }

    fun loadChart(days: Int = DEFAULT_CHART_DAYS) {
        viewModelScope.launch {
            _chartState.value = ChartUiState.Loading
            try {
                val chart = repository.getMarketChart(coinId, days)
                _chartState.value = ChartUiState.Success(chart)
            } catch (e: IOException) {
                _chartState.value = ChartUiState.Error("Chart unavailable")
            } catch (e: Exception) {
                _chartState.value = ChartUiState.Error("Chart unavailable")
            }
        }
    }

    fun retryDetails() = loadDetails()
    fun retryChart(days: Int = DEFAULT_CHART_DAYS) = loadChart(days)
}