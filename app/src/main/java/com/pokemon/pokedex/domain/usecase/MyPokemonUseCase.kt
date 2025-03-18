package com.pokemon.pokedex.domain.usecase

import com.pokemon.pokedex.data.repository.PokemonRepository
import com.pokemon.pokedex.domain.entity.MyPokemon
import com.pokemon.pokedex.domain.entity.PokemonInfo
import io.reactivex.rxjava3.core.Single

class MyPokemonUseCase(private val pokemonRepository: PokemonRepository) {
    operator fun invoke(): Single<List<MyPokemon>> {
        return pokemonRepository.getMyPokemonList()
    }
}