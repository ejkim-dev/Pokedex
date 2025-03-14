package com.cubox.pokedex.presentation.ui

import android.content.Intent
import com.cubox.pokedex.data.PokemonRepository
import com.cubox.pokedex.databinding.FragmentPokemonListBinding
import com.cubox.pokedex.domain.model.MyPokemonHistory
import com.cubox.pokedex.domain.model.PokemonInfo
import com.cubox.pokedex.domain.usecase.AddPokemonHistoryUseCase
import com.cubox.pokedex.domain.usecase.GetPokemonHistoryUseCase
import com.cubox.pokedex.presentation.TimeUtil
import com.cubox.pokedex.presentation.KeyConstant
import com.cubox.pokedex.presentation.adapter.PokemonAdapter
import com.cubox.pokedex.presentation.item.MyPokemonItem
import com.cubox.pokedex.presentation.showToast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class MyPokemonFragment :
    BaseFragment<FragmentPokemonListBinding>(FragmentPokemonListBinding::inflate) {

    private val addPokemonHistoryUseCase: AddPokemonHistoryUseCase by lazy {
        AddPokemonHistoryUseCase(PokemonRepository())
    }

    private val getPokemonHistoryUseCase: GetPokemonHistoryUseCase by lazy {
        GetPokemonHistoryUseCase(PokemonRepository())
    }

    override fun initViews() {
        super.initViews()

        binding.recyclerViewPokemonList.adapter = PokemonAdapter { pokemon ->
            addPokemonHistoryUseCase(pokemon.id)

            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(KeyConstant.POKEMON_ID, pokemon.id)
            intent.putExtra(KeyConstant.POKEMON_NAME, pokemon.name)
            intent.putExtra(KeyConstant.IMAGE, pokemon.imageUrl)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        (activity as? MainActivity)?.run {
            observePokemon()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { createMyPokemonItems(it, getPokemonHistoryUseCase()) }
                .filter { it.isNotEmpty() }
                .subscribe({
                    with(binding.recyclerViewPokemonList) {
                        (adapter as? PokemonAdapter)?.submitList(it)
                        smoothScrollToPosition(0)
                    }
                }) {
                    showToast("observePokemon error : ${it.message}")
                }
                .addTo(disposables)
        }
    }

    private fun createMyPokemonItems(
        pokemonList: List<PokemonInfo>,
        historyList: List<MyPokemonHistory>
    ): List<MyPokemonItem> {
        if (historyList.isEmpty() || pokemonList.isEmpty()) return emptyList()

        val pokemonMap = pokemonList.associateBy { it.id }

        return historyList.mapNotNull { history ->
            pokemonMap[history.id]?.let { pokemon ->
                MyPokemonItem(
                    id = history.id,
                    name = pokemon.name,
                    imageUrl = pokemon.imageUrl,
                    savedTime = TimeUtil.formatTime(history.createTimeMillis),
                    updatedTime = TimeUtil.formatTime(history.updatedTimeMillis),
                    lastClickedTime = history.updatedTimeMillis
                )
            }
        }.sortedByDescending { it.lastClickedTime }
    }
}