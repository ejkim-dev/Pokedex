package com.pokemon.pokedex.domain.usecase

import com.pokemon.pokedex.data.repository.PokemonRepository


class AddPokemonHistoryUseCase(private val pokemonRepository: PokemonRepository) {
    operator fun invoke(pokemonId: Int) = pokemonRepository.addMyPokemonHistory(pokemonId)
}