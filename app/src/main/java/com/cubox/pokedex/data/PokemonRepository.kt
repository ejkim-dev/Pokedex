package com.cubox.pokedex.data

import com.cubox.pokedex.data.entity.PokemonInfoResponse
import com.cubox.pokedex.data.entity.PokemonResponse
import com.cubox.pokedex.data.entity.PokemonSpeciesResponse
import io.reactivex.rxjava3.core.Single

class PokemonRepository {
    private val pokemonService: PokemonApiService
        get() = RetrofitManager.getPokemonService()

    fun getPokemonList(limit: Int, offset: Int): Single<PokemonResponse> {
        return pokemonService.getPokemonList(limit, offset)
    }

    fun getPokemonInfo(id: Int): Single<PokemonInfoResponse> {
        return pokemonService.getPokemonInfo(id)
    }

    fun getPokemonSpecies(id: Int): Single<PokemonSpeciesResponse> {
        return pokemonService.getPokemonSpecies(id)
    }
}