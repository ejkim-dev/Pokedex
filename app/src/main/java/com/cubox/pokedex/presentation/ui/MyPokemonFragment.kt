package com.cubox.pokedex.presentation.ui

import android.util.Log
import com.cubox.pokedex.databinding.FragmentPokemonListBinding
import com.cubox.pokedex.presentation.adapter.PokemonAdapter
import com.cubox.pokedex.presentation.item.MyPokemonItem

class MyPokemonFragment :
    BaseFragment<FragmentPokemonListBinding>(FragmentPokemonListBinding::inflate) {

    override fun initViews() {
        val dummyPokemonList = List(10) { index -> MyPokemonItem(index, "Pokemon $index", "", "${System.currentTimeMillis()}", "${System.currentTimeMillis()}") }
        binding.recyclerViewPokemonList.adapter = PokemonAdapter { pokemon ->
            // Do something with the selected pokemon
            Log.d("######", "initViews: $pokemon")
        }.apply {
            submitList(dummyPokemonList)
        }
    }
}