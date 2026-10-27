package co.edu.uniquindio.pokevault.data.api

import co.edu.uniquindio.pokevault.data.dto.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query



interface PokeApiService {
    // Endpoint 1: Lista paginada
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponseDto

    // Endpoint 2: Detalle por ID o nombre
    @GET("pokemon/{idOrName}")
    suspend fun getPokemonDetail(
        @Path("idOrName") idOrName: String
    ): PokemonDetailDto

    // Endpoint 3: Filtro por tipo
    @GET("type/{typeName}")
    suspend fun getPokemonByType(
        @Path("typeName") typeName: String
    ): PokemonTypeResponseDto

    // Endpoint 4: Descripción de la especie
    @GET("pokemon-species/{idOrName}")
    suspend fun getPokemonSpecies(
        @Path("idOrName") idOrName: String
    ): PokemonSpeciesDto

}