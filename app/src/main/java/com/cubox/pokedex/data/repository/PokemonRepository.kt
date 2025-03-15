package com.cubox.pokedex.data.repository

import com.cubox.pokedex.data.memory.datasource.InMemoryDataSource
import com.cubox.pokedex.data.remote.datasource.PokemonDataSource
import com.cubox.pokedex.data.remote.RetrofitManager
import com.cubox.pokedex.data.remote.model.PokemonInfoResponse
import com.cubox.pokedex.domain.entity.MyPokemon
import com.cubox.pokedex.data.memory.model.MyPokemonHistory
import com.cubox.pokedex.domain.entity.PokemonInfo
import com.cubox.pokedex.domain.entity.PokemonSpecies
import io.reactivex.rxjava3.core.Single

class PokemonRepository {
    private val pokemonDataSource: PokemonDataSource
        get() = RetrofitManager.pokemonDataSource()

    fun getPokemonList(limit: Int, offset: Int): Single<List<PokemonInfo>> {
        return pokemonDataSource.getPokemonList(limit, offset)
            .map { it.results.map { extractIdFromUrl(it.url) } }
            .flatMap { pokemonIds ->
                // ID 각각에 대해  List<Single<PokemonInfo>> 목록을 생성
                val singleList = pokemonIds.map { getPokemonInfo(it) }

                // List<Single<PokemonInfo>>를 zip으로 묶어 합침
                Single.zip(singleList) { arrayOfAny ->
                    arrayOfAny.map { it as PokemonInfo }
                }
            }
    }

    fun getPokemonSpecies(id: Int): Single<PokemonSpecies> {
        return pokemonDataSource.getPokemonSpecies(id)
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

    fun getMyPokemonList(pokemonList: List<PokemonInfo>): List<MyPokemon> {
        if (getMyPokemonHistoryList().isEmpty() || pokemonList.isEmpty()) return emptyList()

        val pokemonMap = pokemonList.associateBy { it.id }

        return getMyPokemonHistoryList().mapNotNull { history ->
            pokemonMap[history.id]?.let { pokemon ->
                MyPokemon(
                    id = history.id,
                    name = pokemon.name,
                    imageUrl = pokemon.imageUrl,
                    savedTime = history.createTimeMillis,
                    updatedTime = history.updatedTimeMillis,
                )
            }
        }
    }

    fun addMyPokemonHistory(pokemonId: Int) {
        return InMemoryDataSource.addMyPokemonHistory(pokemonId)
    }

    private fun getMyPokemonHistoryList(): List<MyPokemonHistory> {
        return InMemoryDataSource.getMyPokemonHistoryList()
    }

    private fun getPokemonInfo(id: Int): Single<PokemonInfo> {
        return pokemonDataSource.getPokemonInfo(id)
            .map { mapToPokemonInfo(it) }
    }

    private fun mapToPokemonInfo(response: PokemonInfoResponse): PokemonInfo {
        return PokemonInfo(
            id = response.id,
            name = response.name,
            types = response.types.map { it.type["name"].orEmpty() },
            height = response.height,
            weight = response.weight,
            baseExperience = response.baseExperience,
            imageUrl = response.imageUrl
        )
    }

    /**
     * URL에서 포켓몬 ID를 추출
     *
     * @param url 포켓몬 리소스 URL (예: "https://pokeapi.co/api/v2/pokemon/25/")
     * @return 추출된 포켓몬 ID
     */
    private fun extractIdFromUrl(url: String): Int {
        return url.split("/").dropLast(1).last().toInt()
    }
}