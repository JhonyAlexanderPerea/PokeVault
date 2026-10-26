package co.edu.uniquindio.pokevault.data.api

import co.edu.uniquindio.pokevault.data.dto.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query



interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int =20,
        @Query("offset") offset: Int = 0
    ): PokemoListResponseDto

    @GET("pokemo/{idOrName}")
    suspend fun getPokemonDetail(@Path("idOrName") idOrName: String): PokemonDetailDto

    @GET("type/{typeName}")
    suspend fun getPokemobyType(@Path("typeName") typeName: String): PokemonTypeResponseDto

    @GET("pokemon-species/{idOrName}")
    suspend fun getPokemonSpecies(@Path("idOrName") idOrName: String): PokemonSpeciesDto

}