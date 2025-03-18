package com.pokemon.pokedex.domain.entity

data class PokemonSpecies(
    val id: Int,
    val color: String,
    val baseHappiness: Int,
    val genera: String,
    val description: String,
    val shape: String
)
