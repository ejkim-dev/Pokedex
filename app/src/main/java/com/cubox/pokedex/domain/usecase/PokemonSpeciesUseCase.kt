package com.cubox.pokedex.domain.usecase

import com.cubox.pokedex.data.repository.PokemonRepository
import com.cubox.pokedex.domain.entity.PokemonSpecies
import io.reactivex.rxjava3.core.Single

class PokemonSpeciesUseCase(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(id: Int): Single<PokemonSpecies> {
        return pokemonRepository.getPokemonSpecies(id)
    }
}