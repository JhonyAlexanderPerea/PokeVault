package co.edu.uniquindio.pokevault.data.dto

import com.google.gson.annotations.SerializedName

data class PokemonListResponseDto(
    @SerializedName("results") val results: List<NamedApiResourceDto>
)

data class NamedApiResourceDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class PokemonDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("types") val types: List<PokemonTypeSlotDto>,
    @SerializedName("abilities") val abilities: List<PokemonAbilitySlotDto>,
    @SerializedName("stats") val stats: List<PokemonStatDto>
)

data class PokemonTypeSlotDto(@SerializedName("type") val type: NamedApiResourceDto)
data class PokemonAbilitySlotDto(@SerializedName("ability") val ability: NamedApiResourceDto)
data class PokemonStatDto(@SerializedName("base_stat") val baseStat: Int, @SerializedName("stat") val stat: NamedApiResourceDto)

data class PokemonTypeResponseDto(
    @SerializedName("pokemon") val pokemon: List<TypePokemonWrapperDto>
)
data class TypePokemonWrapperDto(@SerializedName("pokemon") val pokemon: NamedApiResourceDto)

data class PokemonSpeciesDto(
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntryDto>
)
data class FlavorTextEntryDto(
    @SerializedName("flavor_text") val flavorText: String,
    @SerializedName("language") val language: NamedApiResourceDto
)
