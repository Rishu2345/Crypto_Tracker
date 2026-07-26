package com.buildsol.cryptotracker.presentation.details

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

    private val coinId: String = checkNotNull(savedStateHandle["coinId"]) {
        "CoinDetailViewModel requires a coinId nav argument"
    }

    private val _detailState = MutableStateFlow<CoinDetailUiState>(CoinDetailUiState.Loading)
    val detailState: StateFlow<CoinDetailUiState> = _detailState.asStateFlow()

    private val _chartState = MutableStateFlow<ChartUiState>(ChartUiState.Loading)
    val chartState: StateFlow<ChartUiState> = _chartState.asStateFlow()

    init {
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