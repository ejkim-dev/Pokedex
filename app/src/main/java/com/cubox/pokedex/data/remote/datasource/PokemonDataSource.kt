package com.cubox.pokedex.data.remote.datasource

import com.cubox.pokedex.data.remote.model.PokemonInfoResponse
import com.cubox.pokedex.data.remote.model.PokemonResponse
import com.cubox.pokedex.data.remote.model.PokemonSpeciesResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonDataSource {
    @GET("pokemon")
    fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Single<PokemonResponse>

    @GET("pokemon/{id}")
    fun getPokemonInfo(
        @Path("id") id: Int
    ): Single<PokemonInfoResponse>

    @GET("pokemon-species/{id}")
    fun getPokemonSpecies(
        @Path("id") id: Int
    ): Single<PokemonSpeciesResponse>

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }
}