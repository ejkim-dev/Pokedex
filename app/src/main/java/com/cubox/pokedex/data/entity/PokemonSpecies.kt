package com.cubox.pokedex.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonSpeciesResponse(
    val id: Int,
    val name: String,
    val names: List<NameEntity>,
    @Json(name = "base_happiness")
    val baseHappiness: Int,
    val color: Map<String, String>,
    @Json(name = "flavor_text_entries")
    val flavors: List<FlavorTextEntryEntity>,
    val genera: List<GeneraEntity>,
    val shape: Map<String, String>
)

data class FlavorTextEntryEntity(
    @Json(name = "flavor_text")
    val flavorText: String,
    val language: Map<String, String>,
    val version: Map<String, String>
)

data class GeneraEntity(
    val genus: String,
    val language: Map<String, String>
)

data class NameEntity(
    val language: Map<String, String>,
    val name: String
)