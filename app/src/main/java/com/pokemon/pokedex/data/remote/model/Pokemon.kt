package com.pokemon.pokedex.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonResponse(
    val count: Int,
    val next: String,
    val results: List<ResultResponse>
)

data class ResultResponse(
    val name: String,
    val url: String
)