package com.cubox.pokedex.domain.usecase

import com.cubox.pokedex.data.PokemonRepository
import com.cubox.pokedex.domain.model.PokemonSpecies
import io.reactivex.rxjava3.core.Single

class PokemonSpeciesUseCase {
    private val pokemonRepository: PokemonRepository by lazy {
        PokemonRepository()
    }

    operator fun invoke(id: Int): Single<PokemonSpecies> {
        return pokemonRepository.getPokemonSpecies(id)
            .map {
                var currentGenera = ""
                it.genera.forEach {
                    if (it.language["name"] == "en") {
                        currentGenera = it.genus
                        return@forEach
                    }
                }

                var currentDescription = ""
                it.flavors.forEach {
                    if (it.language["name"] == "en") {
                        currentDescription = it.flavorText
                        return@forEach
                    }
                }

                PokemonSpecies(
                    id = it.id,
                    color = it.color["name"].orEmpty(),
                    baseHappiness = it.baseHappiness,
                    genera = currentGenera,
                    description = currentDescription,
                    shape = it.shape["name"].orEmpty()
                )
            }
    }
}