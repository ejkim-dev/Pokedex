package com.pokemon.pokedex.presentation.ui

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.pokemon.pokedex.data.repository.PokemonRepository
import com.pokemon.pokedex.databinding.FragmentPokemonListBinding
import com.pokemon.pokedex.domain.usecase.AddPokemonHistoryUseCase
import com.pokemon.pokedex.presentation.KeyConstant
import com.pokemon.pokedex.presentation.adapter.PokemonAdapter
import com.pokemon.pokedex.presentation.item.PokemonItem
import com.pokemon.pokedex.presentation.showToast
import com.jakewharton.rxbinding4.view.scrollChangeEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo

class PokemonListFragment :
    BaseFragment<FragmentPokemonListBinding>(FragmentPokemonListBinding::inflate) {

        private val addPokemonHistoryUseCase: AddPokemonHistoryUseCase by lazy {
            AddPokemonHistoryUseCase(PokemonRepository())
        }

    override fun initViews() {
        super.initViews()

        binding.recyclerViewPokemonList.adapter = PokemonAdapter { pokemon ->
            addPokemonHistoryUseCase(pokemon.id)

            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(KeyConstant.POKEMON_ID, pokemon.id)
            intent.putExtra(KeyConstant.POKEMON_NAME, pokemon.name)
            intent.putExtra(KeyConstant.IMAGE, pokemon.imageUrl)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }

    override fun subscribeView() {
        super.subscribeView()

        with(binding) {
            recyclerViewPokemonList.scrollChangeEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val layoutManager = recyclerViewPokemonList.layoutManager as GridLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount


                    (activity as? MainActivity)?.run {
                        if (lastVisibleItemPosition >= totalItemCount - 1 && !isLoading) {
                            getPokemon(offset = totalItemCount)
                        }
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