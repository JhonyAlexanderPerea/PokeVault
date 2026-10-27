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

    override suspend fun getPokemonList(page: Int, query: String, typeFilter: String): Resource<List<Pokemon>> {
        return try {
            if (query.isNotBlank()) {
                val detail = apiService.getPokemonDetail(query.lowercase().trim())
                return Resource.Success(listOf(Pokemon(detail.id, detail.name.capitalize(), getImg(detail.id))))
            }
            if (typeFilter.isNotBlank()) {
                val byType = apiService.getPokemonByType(typeFilter.lowercase().trim())
                val list = byType.pokemon.take(20).map {
                    val id = extractId(it.pokemon.url)
                    Pokemon(id, it.pokemon.name.capitalize(), getImg(id))
                }
                return Resource.Success(list)
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
            val desc = species?.flavorTextEntries?.firstOrNull { it.language.name == "es" || it.language.name == "en" }?.flavorText?.replace("\n", " ") ?: "Sin descripción."
            val stats = dto.stats.associate { it.stat.name to it.baseStat }

            Resource.Success(
                PokemonDetail   (
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
                    description = desc
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