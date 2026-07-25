package com.buildsol.cryptotracker.presentation.coinList

import com.buildsol.cryptotracker.domain.model.SearchCoin

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Success(val results: List<SearchCoin>) : SearchUiState
    data class Error(val message: String) : SearchUiState
    data object Empty : SearchUiState
}