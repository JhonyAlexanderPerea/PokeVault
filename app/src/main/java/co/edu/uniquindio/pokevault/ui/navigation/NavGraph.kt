package co.edu.uniquindio.pokevault.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.edu.uniquindio.pokevault.ui.detail.DetailScreen
import co.edu.uniquindio.pokevault.ui.detail.DetailViewModel
import co.edu.uniquindio.pokevault.ui.list.ListScreen
import co.edu.uniquindio.pokevault.ui.list.ListViewModel

private const val ANIMATION_DURATION = 350

@Composable
fun AppNavigation(isOnline: Boolean) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "list",
        enterTransition = { fadeIn(tween(ANIMATION_DURATION)) + scaleIn(initialScale = 0.95f, animationSpec = tween(ANIMATION_DURATION)) },
        exitTransition = { fadeOut(tween(ANIMATION_DURATION)) + scaleOut(targetScale = 0.95f, animationSpec = tween(ANIMATION_DURATION)) },
        popEnterTransition = { fadeIn(tween(ANIMATION_DURATION)) + scaleIn(initialScale = 0.95f, animationSpec = tween(ANIMATION_DURATION)) },
        popExitTransition = { fadeOut(tween(ANIMATION_DURATION)) + scaleOut(targetScale = 0.95f, animationSpec = tween(ANIMATION_DURATION)) }
    ) {
        composable(route = "list") {
            val viewModel: ListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            ListScreen(
                uiState = uiState,
                isOnline = isOnline,
                onSearchChange = viewModel::onSearchQueryChanged,
                onTypeChange = viewModel::onTypeFilterChanged,
                onLoadMore = viewModel::loadNextPage,
                onRetry = viewModel::retry,
                onClearError = viewModel::clearError,
                onPokemonClick = { id -> navController.navigate("detail/$id") }
            )
        }

        composable(
            route = "detail/{pokemonId}",
            arguments = listOf(navArgument("pokemonId") { type = NavType.StringType })
        ) {
            val viewModel: DetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            DetailScreen(
                uiState = uiState,
                onRetry = viewModel::retry,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
