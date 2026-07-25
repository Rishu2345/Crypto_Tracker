package com.buildsol.cryptotracker.presentation.coinList

import com.buildsol.cryptotracker.domain.model.Coin

sealed interface CoinListUiState {
    data object Loading: CoinListUiState
    data object Empty: CoinListUiState
    data class Success(val coins: List<Coin>, val isLoadingMore: Boolean): CoinListUiState
    data class Error(val message: String): CoinListUiState
}