package com.pokemon.pokedex.data.repository

import com.pokemon.pokedex.data.KeyConstants
import com.pokemon.pokedex.data.memory.datasource.InMemoryDataSource.loadData
import com.pokemon.pokedex.data.memory.datasource.InMemoryDataSource.saveData
import com.pokemon.pokedex.data.remote.datasource.PokemonDataSource
import com.pokemon.pokedex.data.remote.RetrofitManager
import com.pokemon.pokedex.data.remote.model.PokemonInfoResponse
import com.pokemon.pokedex.domain.entity.MyPokemon
import com.pokemon.pokedex.data.memory.model.MyPokemonHistory
import com.pokemon.pokedex.domain.entity.PokemonInfo
import com.pokemon.pokedex.domain.entity.PokemonSpecies
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

    fun getMyPokemonList(): Single<List<MyPokemon>> {
        val pokemonList = getPokemonInfoList()
        if (getMyPokemonHistoryList().isEmpty() || pokemonList.isEmpty()) return Single.just(emptyList())

        val pokemonMap = pokemonList.associateBy { it.id }

        val myPokemonList = getMyPokemonHistoryList().mapNotNull { history ->
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
        return Single.just(myPokemonList)
    }

    fun addMyPokemonHistory(pokemonId: Int) {
        val myPokemonHistoryList =
            loadData(KeyConstants.MY_POKEMON, emptyList<MyPokemonHistory>()).toMutableList()
        val index = myPokemonHistoryList.indexOfFirst { it.id == pokemonId }
        val isExist = index >= 0
        val currentTime = System.currentTimeMillis()

        if (isExist) {
            val currentMyPokemonHistory = myPokemonHistoryList[index]
            // 같은 ID가 이미 있으면 updatedTimeMillis만 갱신
            myPokemonHistoryList[index] =
                currentMyPokemonHistory.copy(updatedTimeMillis = currentTime)
        } else {
            myPokemonHistoryList.add(
                MyPokemonHistory(
                    id = pokemonId,
                    createTimeMillis = currentTime,
                    updatedTimeMillis = currentTime
                )
            )
        }

        return saveData(KeyConstants.MY_POKEMON, myPokemonHistoryList)
    }

    fun savePokemonInfoList(pokemonInfo: List<PokemonInfo>) {
        if (getPokemonInfoList().isEmpty()) {
            saveData(KeyConstants.POKEMON_INFO, pokemonInfo)
        } else {
            val currentPokemonInfoList = getPokemonInfoList().toMutableList()
            currentPokemonInfoList.addAll(pokemonInfo)
            saveData(KeyConstants.POKEMON_INFO, currentPokemonInfoList.toList())
        }
    }

    fun getPokemonInfoList(): List<PokemonInfo> {
        return loadData(KeyConstants.POKEMON_INFO, emptyList())
    }

    private fun getMyPokemonHistoryList(): List<MyPokemonHistory> {
        return loadData(KeyConstants.MY_POKEMON, emptyList())
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