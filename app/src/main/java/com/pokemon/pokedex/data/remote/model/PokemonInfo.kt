package com.pokemon.pokedex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonInfoResponse(
    val id: Int,
    val name: String,
    val types: List<TypeResponse>,
    val height: Double,
    val weight: Double,
    @Json(name = "base_experience")
    val baseExperience: Int
) {
    val imageUrl =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"
}

data class TypeResponse(
    val slot: Int,
    val type: Map<String, String>
)