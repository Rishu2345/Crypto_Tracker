package com.buildsol.cryptotracker.presentation.coinDetails

import com.buildsol.cryptotracker.domain.model.CoinDetails
import com.buildsol.cryptotracker.domain.model.MarketChart

sealed interface CoinDetailUiState {
    data object Loading : CoinDetailUiState
    data class Success(val details: CoinDetails) : CoinDetailUiState
    data class Error(val message: String) : CoinDetailUiState
}

