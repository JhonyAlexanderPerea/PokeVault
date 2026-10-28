package co.edu.uniquindio.pokevault.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.edu.uniquindio.pokevault.ui.components.ItemCard
import co.edu.uniquindio.pokevault.ui.components.SearchAndFilterBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    uiState: ListUiState,
    onSearchChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onLoadMore: () -> Unit,
    onPokemonClick: (String) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("PokéDex Jetpack Compose") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SearchAndFilterBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = onSearchChange,
                selectedType = uiState.selectedType,
                onTypeFilterChange = onTypeChange
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(
                    items = uiState.pokemons,
                    key = { _, pokemon -> pokemon.id }
                ) { index, pokemon ->
                    if (index >= uiState.pokemons.size - 6 && !uiState.isLoadingPage) {
                        SideEffect { onLoadMore() }
                    }

                    ItemCard(
                        pokemon = pokemon,
                        onClick = { onPokemonClick(pokemon.id.toString()) }
                    )
                }
            }
        }
    }
}
