package com.cubox.pokedex.domain

import com.cubox.pokedex.domain.model.MyPokemonHistory

object MyPokemonManager {
    private val myPokemonHistoryList = mutableListOf<MyPokemonHistory>()

    fun getMyPokemonHistoryList(): List<MyPokemonHistory> {
        return myPokemonHistoryList
    }

    fun getMyPokemonHistoryById(pokemonId: Int): MyPokemonHistory? {
        return myPokemonHistoryList.find { it.id == pokemonId }
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

    fun getMyPokemonHistoryCount(): Int {
        return myPokemonHistoryList.size
    }
}