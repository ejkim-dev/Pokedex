package com.pokemon.pokedex.presentation.ui

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.pokemon.pokedex.data.repository.PokemonRepository
import com.pokemon.pokedex.databinding.FragmentPokemonListBinding
import com.pokemon.pokedex.domain.usecase.AddPokemonHistoryUseCase
import com.pokemon.pokedex.presentation.KeyConstant
import com.pokemon.pokedex.presentation.adapter.PokemonAdapter
import com.pokemon.pokedex.presentation.item.PokemonItem
import com.jakewharton.rxbinding4.view.scrollChangeEvents
import com.pokemon.pokedex.domain.usecase.PokemonUseCase
import com.pokemon.pokedex.presentation.hide
import com.pokemon.pokedex.presentation.item.BasePokemonItem
import com.pokemon.pokedex.presentation.show
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class PokemonListFragment :
    BaseFragment<FragmentPokemonListBinding>(FragmentPokemonListBinding::inflate) {
    private val pokemonRepository = PokemonRepository()
    private val pokemonUseCase: PokemonUseCase by lazy { PokemonUseCase(pokemonRepository) }
    private val addPokemonHistoryUseCase: AddPokemonHistoryUseCase by lazy {
        AddPokemonHistoryUseCase(pokemonRepository)
    }

    private val loadingObserver: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun initView() {
        subscribeView()

        binding.recyclerViewPokemonList.adapter = PokemonAdapter { pokemon ->
            addPokemonHistoryUseCase(pokemon.id)
            showDetailActivity(pokemon)
        }
    }

    override fun onResume() {
        super.onResume()

        val hasItem = (binding.recyclerViewPokemonList.adapter?.itemCount ?: 0) > 0
        if (!hasItem) {
            getPokemon(offset = 0)
        }
    }

    private fun subscribeView() {
        with(binding) {
            recyclerViewPokemonList.scrollChangeEvents()
                .map {
                    val layoutManager = recyclerViewPokemonList.layoutManager as GridLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount

                    Pair(lastVisibleItemPosition >= totalItemCount - 1, totalItemCount)
                }
                .subscribe { (islLastItemPosition, totalItemCount) ->
                    if (islLastItemPosition && !loadingObserver.value!!) {
                        getPokemon(offset = totalItemCount)
                    }
                }
                .addTo(disposables)

            loadingObserver.observe(viewLifecycleOwner) {
                if (it) progress.show() else progress.hide()
            }
        }
    }

    private fun getPokemon(limit: Int = 10, offset: Int) {
        pokemonUseCase(limit, offset)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { loadingObserver.postValue(true) }
            .doOnTerminate { loadingObserver.postValue(false) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ currentPokemonInfo ->
                val pokemonItemList =
                    currentPokemonInfo.map { PokemonItem(it.id, it.name, it.imageUrl) }
                val pokemonAdapter = binding.recyclerViewPokemonList.adapter as? PokemonAdapter
                val currentItems = pokemonAdapter?.currentList ?: emptyList()
                pokemonAdapter?.submitList(currentItems + pokemonItemList)
            }, {
                processError("getPokemon error : ${it.message}")
            })
            .addTo(disposables)
    }

    private fun showDetailActivity(pokemon: BasePokemonItem) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(KeyConstant.POKEMON_ID, pokemon.id)
        intent.putExtra(KeyConstant.POKEMON_NAME, pokemon.name)
        intent.putExtra(KeyConstant.IMAGE, pokemon.imageUrl)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }
}