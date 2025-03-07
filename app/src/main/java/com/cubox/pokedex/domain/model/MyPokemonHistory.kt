package com.cubox.pokedex.domain.model

data class MyPokemonHistory(
    val id: Int,
    val createTimeMillis: Long = 0,
    val updatedTimeMillis: Long = 0
)
