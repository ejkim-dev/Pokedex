package com.pokemon.pokedex.presentation.ui

import com.pokemon.pokedex.data.repository.PokemonRepository
import com.pokemon.pokedex.databinding.ActivityDetailBinding
import com.pokemon.pokedex.domain.usecase.PokemonSpeciesUseCase
import com.pokemon.pokedex.presentation.KeyConstant
import com.pokemon.pokedex.presentation.item.PokemonItem
import com.pokemon.pokedex.presentation.setImageUrl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailActivity : BaseActivity<ActivityDetailBinding>(
    bindingFactory = ActivityDetailBinding::inflate,
    needApplyWindowInsets = false
) {

    private val pokemonSpeciesUseCase: PokemonSpeciesUseCase by lazy {
        PokemonSpeciesUseCase(PokemonRepository())
    }

    private val pokemonItem: PokemonItem by lazy {
        PokemonItem(
            id = intent.getIntExtra(KeyConstant.POKEMON_ID, 0),
            name = intent.getStringExtra(KeyConstant.POKEMON_NAME) ?: "",
            imageUrl = intent.getStringExtra(KeyConstant.IMAGE) ?: ""
        )
    }

    override fun initView() {
        checkData()
        getPokemonSpecies()
    }

    private fun checkData() {
        if (pokemonItem.id == 0 || pokemonItem.name.isEmpty() || pokemonItem.imageUrl.isEmpty()) {
            processError("Pokemon data is incomplete")
            finish()
        }
    }

    private fun getPokemonSpecies() {
        pokemonSpeciesUseCase(pokemonItem.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ pokemonSpecies ->
                binding.imageViewPokemon.setImageUrl(pokemonItem.imageUrl)

                binding.textViewName.text = pokemonItem.name
                binding.textViewDescription.text = pokemonSpecies.description
                binding.textViewGenera.text = pokemonSpecies.genera
                binding.textViewShape.text = pokemonSpecies.shape
                binding.textViewColor.text = pokemonSpecies.color
                binding.textViewBasehappiness.text = "${pokemonSpecies.baseHappiness}"
            }) {
                processError(it)
            }
            .addTo(disposables)
    }
}