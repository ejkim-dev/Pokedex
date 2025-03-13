package com.cubox.pokedex.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonInfoResponse(
    val id: Int,
    val name: String,
    val types: List<TypeEntity>,
    val height: Double,
    val weight: Double,
    @Json(name = "base_experience")
    val baseExperience: Int
) {
    val imageUrl =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"
}

data class TypeEntity(
    val slot: Int,
    val type: Map<String, String>
)