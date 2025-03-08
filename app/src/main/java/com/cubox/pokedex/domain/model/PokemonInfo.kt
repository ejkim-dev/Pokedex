package com.cubox.pokedex.domain.model

data class PokemonInfo(
    val id: Int,
    val name: String,
    val types: List<String>,
    val height: Double, //mm
    val weight: Double, //g
    val baseExperience: Int,
    val imageUrl: String
)
