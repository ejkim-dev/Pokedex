package com.cubox.pokedex.domain.usecase

import com.cubox.pokedex.data.repository.PokemonRepository


class AddPokemonHistoryUseCase(private val pokemonRepository: PokemonRepository) {
    operator fun invoke(pokemonId: Int) = pokemonRepository.addMyPokemonHistory(pokemonId)
}