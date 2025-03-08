package com.cubox.pokedex.presentation.ui

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.cubox.pokedex.databinding.FragmentPokemonListBinding
import com.cubox.pokedex.domain.MyPokemonManager
import com.cubox.pokedex.presentation.adapter.PokemonAdapter
import com.cubox.pokedex.presentation.item.PokemonItem
import com.cubox.pokedex.presentation.showToast
import com.jakewharton.rxbinding4.view.scrollChangeEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo

class PokemonListFragment :
    BaseFragment<FragmentPokemonListBinding>(FragmentPokemonListBinding::inflate) {

    override fun initViews() {
        super.initViews()

        binding.recyclerViewPokemonList.adapter = PokemonAdapter { pokemon ->
            MyPokemonManager.addMyPokemonHistory(pokemon.id)
        }
    }

    override fun subscribeView() {
        super.subscribeView()

        with(binding) {
            recyclerViewPokemonList.scrollChangeEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { event ->
                    val layoutManager = recyclerViewPokemonList.layoutManager as GridLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount

                    if (lastVisibleItemPosition >= totalItemCount - 1) {
                        Log.d("######", "subscribeView: ${event.view.scrollY} | $totalItemCount")
                    }
                }
                .addTo(disposables)
        }
    }

    override fun subscribeData() {
        super.subscribeData()

        (activity as? MainActivity)?.run {
            observePokemon()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pokemonList ->
                    val pokemonItemList =
                        pokemonList.map { PokemonItem(it.id, it.name, it.imageUrl) }
                    (binding.recyclerViewPokemonList.adapter as? PokemonAdapter)?.submitList(
                        pokemonItemList
                    )
                }) {
                    showToast("observePokemon error : ${it.message}")
                }
                .addTo(disposables)

        }
    }
}