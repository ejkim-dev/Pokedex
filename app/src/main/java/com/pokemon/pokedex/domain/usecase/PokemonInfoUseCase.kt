package com.pokemon.pokedex.domain.usecase

import com.pokemon.pokedex.data.repository.PokemonRepository
import com.pokemon.pokedex.domain.entity.PokemonInfo

class PokemonInfoUseCase(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(): List<PokemonInfo> {
        return pokemonRepository.getPokemonInfoList()
    }
}