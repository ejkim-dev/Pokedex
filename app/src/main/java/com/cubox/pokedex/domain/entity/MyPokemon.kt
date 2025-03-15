package com.cubox.pokedex.domain.entity

data class MyPokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val savedTime: Long,
    val updatedTime: Long
)
