package com.cubox.pokedex.domain.usecase

import com.cubox.pokedex.data.repository.PokemonRepository
import com.cubox.pokedex.domain.entity.PokemonInfo
import io.reactivex.rxjava3.core.Single

class PokemonUseCase(
    private val pokemonRepository: PokemonRepository
) {

    operator fun invoke(limit: Int, offset: Int): Single<List<PokemonInfo>> {
        return pokemonRepository.getPokemonList(limit, offset)
    }
}