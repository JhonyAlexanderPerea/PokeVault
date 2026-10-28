package co.edu.uniquindio.pokevault.data.repository

import co.edu.uniquindio.pokevault.data.api.PokeApiService
import co.edu.uniquindio.pokevault.domain.model.Pokemon
import co.edu.uniquindio.pokevault.domain.model.PokemonDetail
import co.edu.uniquindio.pokevault.domain.repository.PokemonRepository
import co.edu.uniquindio.pokevault.util.Resource
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService
) : PokemonRepository {

    private var cachedAllPokemons: List<Pokemon>? = null

    private suspend fun getAllPokemons(): List<Pokemon> {
        cachedAllPokemons?.let { return it }
        return try {
            val response = apiService.getPokemonList(limit = 2000, offset = 0)
            val list = response.results.map {
                val id = extractId(it.url)
                Pokemon(id, it.name.capitalize(), getImg(id))
            }
            cachedAllPokemons = list
            list
        } catch (e: Exception) {
            emptyList()
        }
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

    override suspend fun getPokemonList(page: Int, query: String, typeFilter: String): Resource<List<Pokemon>> {
        return try {
            val cleanQuery = query.lowercase().trim()
            val cleanType = typeFilter.lowercase().trim()

            val baseList = if (cleanType.isNotBlank() && cleanType != "todos" && cleanType != "all") {
                val englishType = mapTypeName(cleanType)
                try {
                    val byType = apiService.getPokemonByType(englishType)
                    byType.pokemon.map {
                        val id = extractId(it.pokemon.url)
                        Pokemon(id, it.pokemon.name.capitalize(), getImg(id))
                    }
                } catch (e: Exception) {
                    emptyList()
                }
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

            if (cleanQuery.isNotBlank() || (cleanType.isNotBlank() && cleanType != "todos" && cleanType != "all")) {
                val pageSize = 20
                val startIndex = (page - 1) * pageSize
                val endIndex = minOf(startIndex + pageSize, filteredList.size)
                val pagedList = if (startIndex < filteredList.size) {
                    filteredList.subList(startIndex, endIndex)
                } else {
                    emptyList()
                }
                return Resource.Success(pagedList)
            }

            val offset = (page - 1) * 20
            val response = apiService.getPokemonList(limit = 20, offset = offset)
            val list = response.results.map {
                val id = extractId(it.url)
                Pokemon(id, it.name.capitalize(), getImg(id))
            }
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error("Error al cargar datos: ${e.localizedMessage}", e)
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
            Resource.Error("Error al cargar el detalle", e)
        }
    }

    private fun extractId(url: String) = url.trimEnd('/').split("/").last().toIntOrNull() ?: 1
    private fun getImg(id: Int) = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    private fun String.capitalize() = replaceFirstChar { it.uppercase() }
}
