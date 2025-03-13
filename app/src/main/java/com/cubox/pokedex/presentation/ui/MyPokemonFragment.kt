package com.cubox.pokedex.presentation.ui

import android.content.Intent
import com.cubox.pokedex.databinding.FragmentPokemonListBinding
import com.cubox.pokedex.domain.MyPokemonManager
import com.cubox.pokedex.presentation.TimeUtil
import com.cubox.pokedex.presentation.KeyConstant
import com.cubox.pokedex.presentation.adapter.PokemonAdapter
import com.cubox.pokedex.presentation.item.MyPokemonItem
import com.cubox.pokedex.presentation.showToast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo

class MyPokemonFragment :
    BaseFragment<FragmentPokemonListBinding>(FragmentPokemonListBinding::inflate) {

    override fun initViews() {
        super.initViews()

        binding.recyclerViewPokemonList.adapter = PokemonAdapter { pokemon ->
            MyPokemonManager.addMyPokemonHistory(pokemon.id)

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
                .filter { MyPokemonManager.getMyPokemonHistoryCount() > 0 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pokemonList ->
                    val myPokemonItem = MyPokemonManager.getMyPokemonHistoryList()
                        .map { history ->
                            val currentPokemon =
                                pokemonList.find { it.id == history.id } ?: return@map null
                            MyPokemonItem(
                                id = history.id,
                                name = currentPokemon.name,
                                imageUrl = currentPokemon.imageUrl,
                                savedTime = TimeUtil.formatTime(history.createTimeMillis),
                                updatedTime = TimeUtil.formatTime(history.updatedTimeMillis),
                                lastClickedTime = history.updatedTimeMillis
                            )
                        }
                        .filterNotNull()
                        .sortedByDescending { it.lastClickedTime }

                    if (myPokemonItem.isNotEmpty()) {
                        with(binding.recyclerViewPokemonList) {
                            (adapter as? PokemonAdapter)?.submitList(myPokemonItem)
                            smoothScrollToPosition(0)
                        }
                    }

                }) {
                    showToast("observePokemon error : ${it.message}")
                }
                .addTo(disposables)

        }
    }
}