package co.edu.uniquindio.pokevault.ui.navigation


import android.R.attr.type
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


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            val viewModel: ListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            ListScreen(
                uiState = uiState,
                onSearchChange = viewModel::onSearchQueryChanged,
                onTypeChange = viewModel::onTypeFilterChanged,
                onLoadMore = viewModel::loadNextPage,
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
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}