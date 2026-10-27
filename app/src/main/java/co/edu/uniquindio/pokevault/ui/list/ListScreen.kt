package co.edu.uniquindio.pokevault.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


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
            // Filtros requeridos por 2 campos[cite: 1]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchChange,
                    label = { Text("Nombre") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.selectedType,
                    onValueChange = onTypeChange,
                    label = { Text("Tipo") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Scroll infinito[cite: 2]
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(uiState.pokemons) { index, pokemon ->
                    if (index >= uiState.pokemons.size - 2 && !uiState.isLoadingPage) {
                        SideEffect { onLoadMore() }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPokemonClick(pokemon.id.toString()) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "#${pokemon.id}",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = pokemon.name, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }

                if (uiState.isLoadingPage) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}