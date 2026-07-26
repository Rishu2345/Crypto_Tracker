package com.buildsol.cryptotracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.buildsol.cryptotracker.presentation.details.CoinDetailViewModel
import com.buildsol.cryptotracker.presentation.details.ui.CoinDetailScreen
import com.buildsol.cryptotracker.presentation.coinList.CoinListViewModel
import com.buildsol.cryptotracker.presentation.coinList.ui.CoinListScreen
import org.koin.compose.viewmodel.koinViewModel




@Composable
fun CryptoTrackerNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            val viewModel: CoinListViewModel = koinViewModel()
            CoinListScreen(
                viewModel = viewModel,
                onCoinClick = { coinId ->
                    navController.navigate(Routes.detailRoute(coinId))
                }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("coinId") { type = NavType.StringType })
        ) {
            val viewModel: CoinDetailViewModel = koinViewModel()
            CoinDetailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

private object Routes {
    const val LIST = "list"
    const val DETAIL = "detail/{coinId}"
    fun detailRoute(coinId: String) = "detail/$coinId"
}