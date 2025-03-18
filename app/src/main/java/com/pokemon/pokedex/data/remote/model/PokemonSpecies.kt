package com.pokemon.pokedex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonSpeciesResponse(
    val id: Int,
    val name: String,
    val names: List<NameResponse>,
    @Json(name = "base_happiness")
    val baseHappiness: Int,
    val color: Map<String, String>,
    @Json(name = "flavor_text_entries")
    val flavors: List<FlavorTextEntryResponse>,
    val genera: List<GeneraResponse>,
    val shape: Map<String, String>
)

data class FlavorTextEntryResponse(
    @Json(name = "flavor_text")
    val flavorText: String,
    val language: Map<String, String>,
    val version: Map<String, String>
)

data class GeneraResponse(
    val genus: String,
    val language: Map<String, String>
)

data class NameResponse(
    val language: Map<String, String>,
    val name: String
)