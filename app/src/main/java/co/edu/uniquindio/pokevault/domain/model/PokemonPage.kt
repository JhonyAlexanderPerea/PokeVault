package co.edu.uniquindio.pokevault.domain.model

data class PokemonPage(
    val pokemons: List<Pokemon>,
    val hasMore: Boolean
)
