package com.cubox.pokedex.data

import com.cubox.pokedex.data.entity.PokemonInfoResponse
import com.cubox.pokedex.data.entity.PokemonResponse
import com.cubox.pokedex.data.entity.PokemonSpeciesResponse
import com.cubox.pokedex.domain.model.MyPokemonHistory
import io.reactivex.rxjava3.core.Single

class PokemonRepository {
    private val pokemonService: PokemonApiService
        get() = RetrofitManager.getPokemonService()

    fun getPokemonList(limit: Int, offset: Int): Single<PokemonResponse> {
        return pokemonService.getPokemonList(limit, offset)
    }

    fun getPokemonInfo(id: Int): Single<PokemonInfoResponse> {
        return pokemonService.getPokemonInfo(id)
    }

    fun getPokemonSpecies(id: Int): Single<PokemonSpeciesResponse> {
        return pokemonService.getPokemonSpecies(id)
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

    companion object {
        private val myPokemonHistoryList = mutableListOf<MyPokemonHistory>()
    }
}