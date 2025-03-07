package com.cubox.pokedex.presentation.item

sealed interface BasePokemonItem {
    val id: Int
    val name: String
    val imageUrl: String
}

data class PokemonItem(
    override val id: Int,
    override val name: String,
    override val imageUrl: String
): BasePokemonItem

data class MyPokemonItem(
    override val id: Int,
    override val name: String,
    override val imageUrl: String,
    val savedTime: String,
    val updatedTime: String
): BasePokemonItem
