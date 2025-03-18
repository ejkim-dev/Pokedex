package com.pokemon.pokedex.domain.usecase

import com.pokemon.pokedex.data.repository.PokemonRepository
import com.pokemon.pokedex.domain.entity.PokemonSpecies
import io.reactivex.rxjava3.core.Single

class PokemonSpeciesUseCase(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(id: Int): Single<PokemonSpecies> {
        return pokemonRepository.getPokemonSpecies(id)
    }
}