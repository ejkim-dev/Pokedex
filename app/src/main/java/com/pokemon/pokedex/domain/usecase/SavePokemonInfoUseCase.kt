package com.pokemon.pokedex.domain.usecase

import com.pokemon.pokedex.data.repository.PokemonRepository
import com.pokemon.pokedex.domain.entity.PokemonInfo

class SavePokemonInfoUseCase(
    private val pokemonRepository: PokemonRepository
) {

    operator fun invoke(pokemonInfoList: List<PokemonInfo>) {
        pokemonRepository.savePokemonInfoList(pokemonInfoList)
    }
}