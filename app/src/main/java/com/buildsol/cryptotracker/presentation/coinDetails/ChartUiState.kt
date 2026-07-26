package com.buildsol.cryptotracker.presentation.coinDetails

import com.buildsol.cryptotracker.domain.model.MarketChart

// Deliberately separate from CoinDetailUiState — a chart failure should
// never block or clear the stats card, and vice versa. Keeping these as two
// independent sealed interfaces (rather than nesting chart state inside the
// detail state) is what makes that isolation trivial to express in the UI:
// two independent `when` blocks, two independent StateFlows.
sealed interface ChartUiState {
    data object Loading : ChartUiState
    data class Success(val chart: MarketChart) : ChartUiState
    data class Error(val message: String) : ChartUiState
}