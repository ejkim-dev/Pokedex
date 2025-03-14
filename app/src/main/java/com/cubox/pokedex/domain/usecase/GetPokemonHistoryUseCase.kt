package com.cubox.pokedex.domain.usecase

import com.cubox.pokedex.data.PokemonRepository
import com.cubox.pokedex.domain.model.MyPokemonHistory

class GetPokemonHistoryUseCase(private val pokemonRepository: PokemonRepository) {
    operator fun invoke(): List<MyPokemonHistory> = pokemonRepository.getMyPokemonHistoryList()
}