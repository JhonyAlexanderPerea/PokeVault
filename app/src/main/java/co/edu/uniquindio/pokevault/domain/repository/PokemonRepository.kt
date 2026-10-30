package co.edu.uniquindio.pokevault.domain.repository

import co.edu.uniquindio.pokevault.domain.model.Pokemon

import co.edu.uniquindio.pokevault.domain.model.PokemonDetail
import co.edu.uniquindio.pokevault.domain.model.PokemonPage
import co.edu.uniquindio.pokevault.util.Resource


interface PokemonRepository{

    suspend fun getPokemonList(
        page: Int,
        query: String = "",
        typeFilter: String = ""
    ): Resource<PokemonPage>

    suspend fun getPokemonDetail(idOrName: String): Resource<PokemonDetail>
}