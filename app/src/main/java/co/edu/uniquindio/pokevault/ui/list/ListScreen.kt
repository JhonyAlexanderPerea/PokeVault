package co.edu.uniquindio.pokevault.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.edu.uniquindio.pokevault.ui.components.ConnectionBanner
import co.edu.uniquindio.pokevault.ui.components.ItemCard
import co.edu.uniquindio.pokevault.ui.components.SearchAndFilterBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    uiState: ListUiState,
    isOnline: Boolean,
    onSearchChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    onClearError: () -> Unit,
    onPokemonClick: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val total = layoutInfo.totalItemsCount
            total > 0 && lastVisible >= total - 6
        }
    }

    LaunchedEffect(
        shouldLoadMore,
        uiState.canLoadMore,
        uiState.isLoadingPage,
        uiState.pokemons.size,
        uiState.error
    ) {
        if (shouldLoadMore && uiState.canLoadMore && !uiState.isLoadingPage && uiState.error == null) {
            onLoadMore()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { errorMsg ->
            if (uiState.pokemons.isNotEmpty()) {
                val result = snackbarHostState.showSnackbar(
                    message = errorMsg,
                    actionLabel = "Reintentar",
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) {
                    onRetry()
                } else {
                    onClearError()
                }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("PokéDex Jetpack Compose") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ConnectionBanner(isOnline = isOnline)

            SearchAndFilterBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = onSearchChange,
                selectedType = uiState.selectedType,
                onTypeFilterChange = onTypeChange
            )

            if (uiState.error != null && uiState.pokemons.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Error al cargar datos",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = uiState.error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(onClick = onRetry) {
                            Text("Reintentar")
                        }
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.pokemons,
                        key = { pokemon -> pokemon.id }
                    ) { pokemon ->
                        ItemCard(
                            pokemon = pokemon,
                            onClick = { onPokemonClick(pokemon.id.toString()) }
                        )
                    }

                    if (uiState.isLoadingPage && uiState.canLoadMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(strokeWidth = 2.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}
