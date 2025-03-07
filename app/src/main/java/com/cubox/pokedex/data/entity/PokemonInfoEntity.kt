package com.cubox.pokedex.data.entity

import com.google.gson.annotations.SerializedName

data class PokemonInfoEntity(
    val id: Int,
    val name: String,
    val types: List<Type>,
    val height: Double,
    val weight: Double,
    val sprites: Sprites,
    val species: Map<String, String>,
    @SerializedName("base_experience")
    val baseExperience: Int
)

data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String,
    val other: Other
)

data class Other(
    @SerializedName("official-artwork")
    val officialArtwork: Map<String, String>,
)

data class Type(
    val slot: Int,
    val type: Map<String, String>
)