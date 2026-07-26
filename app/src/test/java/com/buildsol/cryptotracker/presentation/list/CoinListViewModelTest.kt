package com.buildsol.cryptotracker.presentation.list

import com.buildsol.cryptotracker.domain.model.Coin
import com.buildsol.cryptotracker.domain.model.SearchCoin
import com.buildsol.cryptotracker.domain.repository.CoinRepository
import com.buildsol.cryptotracker.presentation.coinList.CoinListUiState
import com.buildsol.cryptotracker.presentation.coinList.CoinListViewModel
import com.buildsol.cryptotracker.presentation.coinList.SearchUiState
import com.buildsol.cryptotracker.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class CoinListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: CoinRepository
    private lateinit var viewModel: CoinListViewModel

    private fun sampleCoins(page: Int) = listOf(
        Coin(
            id = "coin-$page",
            symbol = "C$page",
            name = "Coin $page",
            image = "",
            price = 10.0,
            marketCap = 1000.0,
            rank = page,
            priceChangePercentage24h = 1.0
        )
    )

    @Before
    fun setup() {
        repository = mockk()
        coEvery { repository.getMarket(page = 1) } returns sampleCoins(1)
        coEvery { repository.loadCoinIndex() } returns Unit
        every { repository.autocomplete(any()) } returns emptyList()
    }

    @Test
    fun `search does not fire before the 300ms debounce window elapses`() = runTest {
        viewModel = CoinListViewModel(repository)
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("bit")


        advanceTimeBy(299.milliseconds)


        coVerify(exactly = 0) { repository.search(any()) }
    }

    @Test
    fun `search fires exactly once after the debounce window elapses`() = runTest {
        coEvery { repository.search("bit") } returns emptyList()
        viewModel = CoinListViewModel(repository)
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("bit")
        advanceTimeBy(300.milliseconds)
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.search("bit") }
    }

    
    @Test
    fun `rapid typing only searches the final query, not every keystroke`() = runTest {
        coEvery { repository.search("bitcoin") } returns emptyList()
        viewModel = CoinListViewModel(repository)
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("b")
        advanceTimeBy(100.milliseconds)
        viewModel.onSearchQueryChanged("bi")
        advanceTimeBy(100.milliseconds)
        viewModel.onSearchQueryChanged("bitcoin")
        advanceTimeBy(300.milliseconds)
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.search("bitcoin") }
        coVerify(exactly = 0) { repository.search("b") }
        coVerify(exactly = 0) { repository.search("bi") }
    }

    @Test
    fun `clearing search restores the prior paginated list untouched`() = runTest {
        coEvery { repository.search("bit") } returns listOf(
            SearchCoin(
                id = "bitcoin", symbol = "btc", name = "Bitcoin",
                image = "",
                marketCapRank = 0
            )
        )
        viewModel = CoinListViewModel(repository)
        advanceUntilIdle()

        val listStateBeforeSearch = viewModel.listState.value

        viewModel.onSearchQueryChanged("bit")

        advanceTimeBy(300.milliseconds)
        advanceUntilIdle()

        assertTrue(viewModel.searchState.value is SearchUiState.Success)

        viewModel.clearSearch()


        assertEquals("", viewModel.searchQuery.value)
        assertEquals(SearchUiState.Idle, viewModel.searchState.value)
        assertEquals(listStateBeforeSearch, viewModel.listState.value)

        coVerify(exactly = 1) { repository.getMarket(page = 1) }
    }

    @Test
    fun `retry re-requests the same page that just failed`() = runTest {
        coEvery { repository.getMarket(page = 1) } throws IOException("offline")

        viewModel = CoinListViewModel(repository)
        advanceUntilIdle()

        assertTrue(viewModel.listState.value is CoinListUiState.Error)

        coEvery { repository.getMarket(page = 1) } returns sampleCoins(1)

        viewModel.retry()
        advanceUntilIdle()

        assertTrue(viewModel.listState.value is CoinListUiState.Success)
        coVerify(exactly = 2) { repository.getMarket(page = 1) }
    }
}