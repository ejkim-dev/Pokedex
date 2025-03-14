package com.cubox.pokedex.presentation.ui

import android.content.Intent
import com.cubox.pokedex.data.repository.PokemonRepository
import com.cubox.pokedex.databinding.FragmentPokemonListBinding
import com.cubox.pokedex.domain.entity.MyPokemon
import com.cubox.pokedex.domain.usecase.AddPokemonHistoryUseCase
import com.cubox.pokedex.domain.usecase.MyPokemonUseCase
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

    private val myPokemonUseCase: MyPokemonUseCase by lazy {
        MyPokemonUseCase(PokemonRepository())
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
                .map { myPokemonUseCase(it) }
                .filter { it.isNotEmpty() }
                .subscribe({
                    with(binding.recyclerViewPokemonList) {
                        (adapter as? PokemonAdapter)?.submitList(createMyPokemonItems(it))
                        smoothScrollToPosition(0)
                    }
                }) {
                    showToast("observePokemon error : ${it.message}")
                }
                .addTo(disposables)
        }
    }

    private fun createMyPokemonItems(
        myPokemonList: List<MyPokemon>
    ): List<MyPokemonItem> {
        return myPokemonList.map { myPokemon ->
            MyPokemonItem(
                id = myPokemon.id,
                name = myPokemon.name,
                imageUrl = myPokemon.imageUrl,
                savedTime = TimeUtil.formatTime(myPokemon.savedTime),
                updatedTime = TimeUtil.formatTime(myPokemon.updatedTime),
                lastClickedTime = myPokemon.updatedTime
            )
        }.sortedByDescending { it.lastClickedTime }
    }
}