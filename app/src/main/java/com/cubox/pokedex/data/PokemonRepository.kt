package com.cubox.pokedex.data

import com.cubox.pokedex.data.entity.PokemonInfoResponse
import com.cubox.pokedex.domain.model.MyPokemonHistory
import com.cubox.pokedex.domain.model.PokemonInfo
import com.cubox.pokedex.domain.model.PokemonSpecies
import io.reactivex.rxjava3.core.Single

class PokemonRepository {
    private val pokemonService: PokemonApiService
        get() = RetrofitManager.getPokemonService()

    fun getPokemonList(limit: Int, offset: Int): Single<List<PokemonInfo>> {
        return pokemonService.getPokemonList(limit, offset)
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
        return pokemonService.getPokemonSpecies(id)
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

    fun getMyPokemonHistoryList(): List<MyPokemonHistory> {
        return myPokemonHistoryList
    }

    fun addMyPokemonHistory(pokemonId: Int) {
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
    }

    fun deleteMyPokemonHistoryById(pokemonId: Int) {
        val myPokemonHistory = myPokemonHistoryList.find { it.id == pokemonId }
        myPokemonHistory?.let {
            myPokemonHistoryList.remove(it)
        }
    }

    fun clearMyPokemonHistory() {
        myPokemonHistoryList.clear()
    }

    private fun getPokemonInfo(id: Int): Single<PokemonInfo> {
        return pokemonService.getPokemonInfo(id)
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

    companion object {
        private val myPokemonHistoryList = mutableListOf<MyPokemonHistory>()
    }
}