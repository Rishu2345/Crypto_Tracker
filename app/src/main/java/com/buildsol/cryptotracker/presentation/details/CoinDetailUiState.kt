package com.buildsol.cryptotracker.presentation.details

import com.buildsol.cryptotracker.domain.model.CoinDetails

sealed interface CoinDetailUiState {
    data object Loading : CoinDetailUiState
    data class Success(val details: CoinDetails) : CoinDetailUiState
    data class Error(val message: String) : CoinDetailUiState
}

