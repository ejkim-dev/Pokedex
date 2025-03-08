package com.cubox.pokedex.presentation.ui

import android.util.Log
import com.cubox.pokedex.databinding.FragmentPokemonListBinding
import com.cubox.pokedex.presentation.adapter.PokemonAdapter
import com.cubox.pokedex.presentation.item.PokemonItem
import com.cubox.pokedex.presentation.showToast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo

class PokemonListFragment :
    BaseFragment<FragmentPokemonListBinding>(FragmentPokemonListBinding::inflate) {

    override fun initViews() {
        binding.recyclerViewPokemonList.adapter = PokemonAdapter { pokemon ->
            // Do something with the selected pokemon
            Log.d("######", "initViews: $pokemon")
        }
    }

    override fun subscribeData() {
        super.subscribeData()

        (activity as? MainActivity)?.run {
            observePokemon()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pokemonList ->
                    val pokemonItemList = pokemonList.map { PokemonItem(it.id, it.name, it.imageUrl) }
                    (binding.recyclerViewPokemonList.adapter as? PokemonAdapter)?.submitList(pokemonItemList)
                }) {
                    showToast("observePokemon error : ${it.message}")
                }
                .addTo(disposables)

        }
    }
}