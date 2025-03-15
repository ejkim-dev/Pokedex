package com.cubox.pokedex.domain.usecase

import com.cubox.pokedex.data.repository.PokemonRepository
import com.cubox.pokedex.domain.entity.MyPokemon
import com.cubox.pokedex.domain.entity.PokemonInfo

class MyPokemonUseCase(private val pokemonRepository: PokemonRepository) {
    operator fun invoke(pokemonList: List<PokemonInfo>): List<MyPokemon> {
        return pokemonRepository.getMyPokemonList(pokemonList)
    }
}