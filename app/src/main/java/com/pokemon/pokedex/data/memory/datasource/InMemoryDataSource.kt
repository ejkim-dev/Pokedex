package com.pokemon.pokedex.data.memory.datasource

object InMemoryDataSource {
    private val inMemoryPokemonStore = mutableMapOf<String, Any>()

    fun saveData(key: String, value: Any) {
        inMemoryPokemonStore[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> loadData(key: String, defaultValue: T): T {
        return inMemoryPokemonStore[key] as? T ?: defaultValue
    }

    fun deleteData(key: String) {
        inMemoryPokemonStore.remove(key)
    }

    fun clearAll() {
        inMemoryPokemonStore.clear()
    }
}