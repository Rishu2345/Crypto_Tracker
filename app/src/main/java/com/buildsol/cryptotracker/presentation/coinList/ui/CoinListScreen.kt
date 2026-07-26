package com.buildsol.cryptotracker.presentation.coinList.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.buildsol.cryptotracker.domain.model.CoinIndexItem
import com.buildsol.cryptotracker.presentation.coinList.CoinListUiState
import com.buildsol.cryptotracker.presentation.coinList.CoinListViewModel
import com.buildsol.cryptotracker.presentation.coinList.SearchUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    viewModel: CoinListViewModel,
    onCoinClick: (coinId: String) -> Unit = {}
) {
    val listState by viewModel.listState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val suggestions by viewModel.autocompleteSuggestions.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val lazyListState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleIndex >= totalItems - 5
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && searchQuery.isBlank()) {
            viewModel.loadNextPage()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Crypto Tracker") },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            CoinSearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                onClear = viewModel::clearSearch,
                suggestions = suggestions,
                onSuggestionSelected = { item: CoinIndexItem ->
                    viewModel.clearSearch()
                    onCoinClick(item.id)
                },
                modifier = Modifier.padding(vertical = 12.dp)
            )

            if (searchQuery.isNotBlank()) {
                SearchResultsContent(
                    state = searchState,
                    query = searchQuery,
                    onCoinClick = onCoinClick
                )
            } else {
                PaginatedListContent(
                    state = listState,
                    isRefreshing = isRefreshing,
                    lazyListState = lazyListState,
                    onRefresh = viewModel::refresh,
                    onRetry = viewModel::retry,
                    onCoinClick = onCoinClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaginatedListContent(
    state: CoinListUiState,
    isRefreshing: Boolean,
    lazyListState: LazyListState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onCoinClick: (String) -> Unit
) {
    when (state) {
        is CoinListUiState.Loading -> {
            ShimmerCoinList(12)
        }

        is CoinListUiState.Success -> {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(state.coins, key = { it.id }) { coin ->
                        CoinRow(coin = coin, onClick = { onCoinClick(coin.id) })
                    }

                    if (state.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        is CoinListUiState.Error -> {
            ErrorContent(message = state.message, onRetry = onRetry)
        }

        is CoinListUiState.Empty -> {
            EmptyContent(
                icon = Icons.Filled.SearchOff,
                message = "No coins available right now."
            )
        }
    }
}

@Composable
private fun SearchResultsContent(
    state: SearchUiState,
    query: String,
    onCoinClick: (String) -> Unit
) {
    when (state) {
        is SearchUiState.Idle -> Unit
        is SearchUiState.Loading -> ShimmerCoinList(count = 5)
        is SearchUiState.Success -> {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.results, key = { it.id }) { result ->

                    ListItem(
                        modifier = Modifier
                            .clickable { onCoinClick(result.id) }
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp,MaterialTheme.colorScheme.outline,RoundedCornerShape(12.dp)),
                        leadingContent = {
                            AsyncImage(
                                model = result.image,
                                contentDescription = result.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                            )
                        },
                        headlineContent = { Text(result.name) },
                        supportingContent = { Text(result.symbol.uppercase()) }
                    )
                }
            }
        }
        is SearchUiState.Error -> ErrorContent(message = state.message, onRetry = null)
        is SearchUiState.Empty -> EmptyContent(
            icon = Icons.Filled.SearchOff,
            message = "No coins match '$query'"
        )
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: (() -> Unit)?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.WifiOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp, bottom = 16.dp)
        )
        if (onRetry != null) {
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun EmptyContent(
    icon: ImageVector,
    message: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}