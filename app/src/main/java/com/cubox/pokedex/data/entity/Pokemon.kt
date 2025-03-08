package com.cubox.pokedex.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonResponse(
    val count: Int,
    val next: String,
    val results: List<ResultEntity>
)

data class ResultEntity(
    val name: String,
    val url: String
)