package co.edu.uniquindio.pokevault.domain.repository

import co.edu.uniquindio.pokevault.domain.model.Pokemon

import co.edu.uniquindio.pokevault.domain.model.PokemonDetail
import co.edu.uniquindio.pokevault.util.Resource


interface PokemonRepository{

    /**
     * Obtiene una lista de pokemons con soporte de paginacion y 2 filtros
     * @param page Número de página para scroll infinito.
     * @param query Campo 1: Búsqueda por nombre de Pokémon.
     * @param typeFilter Campo 2: Filtro por tipo (fuego,agua,etc).
     */

    suspend fun getPokemonList(
        page: Int,
        query: String = "",
        typeFilter: String = ""
    ): Resource<List<Pokemon>>

    /**
     * Obtiene la información extendida de un Pokémon específico.
     * @param idOrName Identificador o nombre del Pokémon.
     */

    suspend fun getPokemonDetail(idOrName: String): Resource<PokemonDetail>
}