package co.edu.uniquindio.pokevault.ui.detail

import co.edu.uniquindio.pokevault.domain.model.PokemonDetail

data class DetailUiState (
    val pokemon: PokemonDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)