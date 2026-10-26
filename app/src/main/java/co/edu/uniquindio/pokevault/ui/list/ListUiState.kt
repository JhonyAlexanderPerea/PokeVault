package co.edu.uniquindio.pokevault.ui.list

import co.edu.uniquindio.pokevault.domain.model.Pokemon
import retrofit2.http.Query

data class ListUiState (
    val pokemons: List<Pokemon> = emptyList(),
    val searchQuery: String = "",
    val selectedType: String = "",
    val isLoadingPage: Boolean = true,
    val currentPage: Int = 1,
    val canLoadMore: Boolean = true,
    val error: String? = null
)