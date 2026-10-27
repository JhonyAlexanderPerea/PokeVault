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
){
    Scaffold(
        topBar = { TopAppBar(title = { Text("PokeDex")}) }
    ) { padding ->
        Column (modifier = Modifier.fillMaxSize().padding(padding)){
            Row(modifier = Modifier.fillMaxSize().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)){

            }
        }

    }
}