package com.pokemon.pokedex.domain.usecase

import com.pokemon.pokedex.data.repository.PokemonRepository
import com.pokemon.pokedex.domain.entity.MyPokemon
import com.pokemon.pokedex.domain.entity.PokemonInfo

class MyPokemonUseCase(private val pokemonRepository: PokemonRepository) {
    operator fun invoke(pokemonList: List<PokemonInfo>): List<MyPokemon> {
        return pokemonRepository.getMyPokemonList(pokemonList)
    }
}