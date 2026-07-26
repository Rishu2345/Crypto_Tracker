package com.buildsol.cryptotracker.presentation.details

import com.buildsol.cryptotracker.domain.model.MarketChart

sealed interface ChartUiState {
    data object Loading : ChartUiState
    data class Success(val chart: MarketChart) : ChartUiState
    data class Error(val message: String) : ChartUiState
}