package co.edu.uniquindio.pokevault.domain.repository

import co.edu.uniquindio.pokevault.domain.model.Pokemon

import co.edu.uniquindio.pokevault.domain.model.PokemonDetail
import co.edu.uniquindio.pokevault.util.Resource


interface PokemonRepository{

    suspend fun getPokemonList(
        page: Int,
        query: String = "",
        typeFilter: String = ""
    ): Resource<List<Pokemon>>

    suspend fun getPokemonDetail(idOrName: String): Resource<PokemonDetail>
}