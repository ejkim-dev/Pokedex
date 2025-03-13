package com.cubox.pokedex.domain.usecase

import com.cubox.pokedex.data.PokemonRepository
import com.cubox.pokedex.domain.model.PokemonInfo
import io.reactivex.rxjava3.core.Single

class PokemonUseCase(
) {
    private val pokemonRepository: PokemonRepository by lazy {
        PokemonRepository()
    }

    operator fun invoke(limit: Int, offset: Int): Single<List<PokemonInfo>> {
        return pokemonRepository.getPokemonList(limit, offset)
            .map { response ->
                response.results
                    .map { it.url.split("/").dropLast(1).last().toInt() }
            }
            .flatMap { pokemonIds ->
                // ID 각각에 대해 Single<PokemonInfo> 목록을 생성
                val singleList = pokemonIds.map { id ->
                    pokemonRepository.getPokemonInfo(id)
                        .map { response ->
                            PokemonInfo(
                                id = response.id,
                                name = response.name,
                                types = response.types.map { it.type["name"].orEmpty() },
                                height = response.height,
                                weight = response.weight,
                                baseExperience = response.baseExperience,
                                imageUrl = response.imageUrl
                            )
                        }
                }

                // 만약 ID가 없는 경우 -> 그냥 빈 리스트 Single 반환
                if (singleList.isEmpty()) {
                    Single.just(emptyList())
                } else {
                    // 여러 Single<PokemonInfo>를 zip으로 묶어 합침
                    Single.zip(singleList) { arrayOfAny ->
                        arrayOfAny.map { it as PokemonInfo }
                    }
                }
            }
    }
}