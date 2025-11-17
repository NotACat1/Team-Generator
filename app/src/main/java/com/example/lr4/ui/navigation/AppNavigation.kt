package com.example.lr4.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lr4.ui.screens.PlayerListScreen
import com.example.lr4.ui.screens.TeamResultScreen
import com.example.lr4.viewmodel.PlayerViewModel

/**
 * Определение маршрутов приложения.
 */
object Routes {
    const val PLAYER_LIST = "player_list"
    const val TEAM_RESULT = "team_result"
}

/**
 * Главный навигационный граф приложения.
 *
 * Управляет переходами между экранами списка игроков и результата распределения команд.
 * Использует общий экземпляр [PlayerViewModel], предоставляемый через Hilt.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: PlayerViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Routes.PLAYER_LIST) {
        composable(Routes.PLAYER_LIST) {
            PlayerListScreen(
                viewModel = viewModel,
                onNavigateToResults = { navController.navigate(Routes.TEAM_RESULT) }
            )
        }
        composable(Routes.TEAM_RESULT) {
            TeamResultScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
