package com.cubox.pokedex.data.entity

import com.google.gson.annotations.SerializedName

data class PokemonSpeciesEntity(
    val id: Int,
    val name: String,
    val names: List<Name>,
    @SerializedName("base_happiness")
    val baseHappiness: Int,
    val color: Map<String, String>,
    @SerializedName("flavor_text_entries")
    val flavors: List<FlavorTextEntry>,
    val genera: List<Genera>,
    val shape: Map<String, String>
)

data class FlavorTextEntry(
    @SerializedName("flavor_text")
    val flavorText: String,
    val language: Map<String, String>,
    val version: Map<String, String>
)

data class Genera(
    val genus: String,
    val language: Map<String, String>
)

data class Name(
    val language: Map<String, String>,
    val name: String
)