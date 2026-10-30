package co.edu.uniquindio.pokevault.data.repository

import co.edu.uniquindio.pokevault.data.api.PokeApiService
import co.edu.uniquindio.pokevault.domain.model.Pokemon
import co.edu.uniquindio.pokevault.domain.model.PokemonDetail
import co.edu.uniquindio.pokevault.domain.model.PokemonPage
import co.edu.uniquindio.pokevault.domain.repository.PokemonRepository
import co.edu.uniquindio.pokevault.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService
) : PokemonRepository {

    private companion object {
        const val PAGE_SIZE = 20
        const val MAX_CACHED_PAGES = 100
    }

    private data class PageKey(
        val page: Int,
        val query: String,
        val type: String
    )

    private var cachedAllPokemons: List<Pokemon>? = null
    private val cachedTypeLists = mutableMapOf<String, List<Pokemon>>()
    private val cachedTypes = mutableMapOf<Int, List<String>>()
    private val cachedPages = mutableMapOf<PageKey, PokemonPage>()

    private suspend fun getAllPokemons(): List<Pokemon> {
        cachedAllPokemons?.let { return it }
        val response = apiService.getPokemonList(limit = 2000, offset = 0)
        val list = response.results.map {
            val id = extractId(it.url)
            Pokemon(id, it.name.capitalize(), getImg(id))
        }
        cachedAllPokemons = list
        return list
    }

    private suspend fun getPokemonsByType(cleanType: String): List<Pokemon> {
        val englishType = mapTypeName(cleanType)
        cachedTypeLists[englishType]?.let { return it }
        val byType = apiService.getPokemonByType(englishType)
        val list = byType.pokemon.map {
            val id = extractId(it.pokemon.url)
            Pokemon(id, it.pokemon.name.capitalize(), getImg(id), listOf(englishType))
        }
        cachedTypeLists[englishType] = list
        return list
    }

    private suspend fun getTypes(id: Int): List<String> {
        cachedTypes[id]?.let { return it }
        val types = runCatching {
            apiService.getPokemonDetail(id.toString()).types.map { it.type.name }
        }.getOrNull().orEmpty()
        if (types.isNotEmpty()) cachedTypes[id] = types
        return types
    }

    private fun mapTypeName(input: String): String {
        val clean = input.lowercase().trim()
        return when (clean) {
            "fuego", "fire" -> "fire"
            "agua", "water" -> "water"
            "planta", "hierba", "grass" -> "grass"
            "electrico", "eléctrico", "electric" -> "electric"
            "hielo", "ice" -> "ice"
            "lucha", "fighting" -> "fighting"
            "veneno", "poison" -> "poison"
            "tierra", "ground" -> "ground"
            "volador", "flying" -> "flying"
            "psiquico", "psíquico", "psychic" -> "psychic"
            "bicho", "bug" -> "bug"
            "roca", "rock" -> "rock"
            "fantasma", "ghost" -> "ghost"
            "dragon", "dragón" -> "dragon"
            "siniestro", "dark" -> "dark"
            "acero", "steel" -> "steel"
            "hada", "fairy" -> "fairy"
            "normal" -> "normal"
            else -> clean
        }
    }

    override suspend fun getPokemonList(page: Int, query: String, typeFilter: String): Resource<PokemonPage> {
        return try {
            val cleanQuery = query.lowercase().trim()
            val cleanType = typeFilter.lowercase().trim()
            val hasTypeFilter = cleanType.isNotBlank() && cleanType != "todos" && cleanType != "all"
            val cacheKey = PageKey(page, cleanQuery, cleanType)
            cachedPages[cacheKey]?.let { return Resource.Success(it) }

            val baseList = if (hasTypeFilter) {
                getPokemonsByType(cleanType)
            } else {
                getAllPokemons()
            }

            val filteredList = if (cleanQuery.isNotBlank()) {
                baseList.filter {
                    it.name.lowercase().contains(cleanQuery) || it.id.toString() == cleanQuery
                }
            } else {
                baseList
            }

            val startIndex = (page - 1) * PAGE_SIZE
            val endIndex = minOf(startIndex + PAGE_SIZE, filteredList.size)
            val slice = if (startIndex < filteredList.size) {
                filteredList.subList(startIndex, endIndex)
            } else {
                emptyList()
            }

            val pageItems = if (hasTypeFilter) {
                slice
            } else {
                coroutineScope {
                    slice.map { pokemon ->
                        async { pokemon.copy(types = getTypes(pokemon.id)) }
                    }.awaitAll()
                }
            }

            val result = PokemonPage(
                pokemons = pageItems,
                hasMore = endIndex < filteredList.size
            )

            if (cachedPages.size >= MAX_CACHED_PAGES) cachedPages.clear()
            cachedPages[cacheKey] = result

            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(mapErrorMessage(e), e)
        }
    }

    override suspend fun getPokemonDetail(idOrName: String): Resource<PokemonDetail> {
        return try {
            val dto = apiService.getPokemonDetail(idOrName.lowercase().trim())
            val species = runCatching { apiService.getPokemonSpecies(idOrName.lowercase().trim()) }.getOrNull()
            val descEs = species?.flavorTextEntries?.firstOrNull { it.language.name == "es" }?.flavorText?.replace("\n", " ") ?: "Sin descripción en español."
            val descEn = species?.flavorTextEntries?.firstOrNull { it.language.name == "en" }?.flavorText?.replace("\n", " ") ?: "No English description available."
            val stats = dto.stats.associate { it.stat.name to it.baseStat }

            Resource.Success(
                PokemonDetail(
                    id = dto.id,
                    name = dto.name.capitalize(),
                    imageUrl = getImg(dto.id),
                    height = dto.height,
                    weight = dto.weight,
                    types = dto.types.map { it.type.name },
                    abilities = dto.abilities.map { it.ability.name },
                    hp = stats["hp"] ?: 0,
                    attack = stats["attack"] ?: 0,
                    defense = stats["defense"] ?: 0,
                    speed = stats["speed"] ?: 0,
                    description = descEs,
                    descriptionEs = descEs,
                    descriptionEn = descEn
                )
            )
        } catch (e: Exception) {
            Resource.Error(mapErrorMessage(e), e)
        }
    }

    private fun mapErrorMessage(e: Exception): String {
        return when (e) {
            is java.net.UnknownHostException, is java.io.IOException ->
                "No hay conexión a Internet. Verifica tu red e intenta nuevamente."
            is retrofit2.HttpException ->
                if (e.code() == 404) "No se encontró el Pokémon solicitado."
                else "Error en el servidor (${e.code()}). Intenta más tarde."
            else -> e.localizedMessage ?: "Ocurrió un error inesperado al procesar la solicitud."
        }
    }

    private fun extractId(url: String) = url.trimEnd('/').split("/").last().toIntOrNull() ?: 1
    private fun getImg(id: Int) = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    private fun String.capitalize() = replaceFirstChar { it.uppercase() }
}
