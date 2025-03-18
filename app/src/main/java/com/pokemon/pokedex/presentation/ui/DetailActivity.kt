package com.pokemon.pokedex.presentation.ui

import com.pokemon.pokedex.data.repository.PokemonRepository
import com.pokemon.pokedex.databinding.ActivityDetailBinding
import com.pokemon.pokedex.domain.usecase.PokemonSpeciesUseCase
import com.pokemon.pokedex.presentation.KeyConstant
import com.pokemon.pokedex.presentation.setImageUrl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailActivity : BaseActivity<ActivityDetailBinding>(ActivityDetailBinding::inflate) {

    private val pokemonSpeciesUseCase: PokemonSpeciesUseCase by lazy {
        PokemonSpeciesUseCase(PokemonRepository())
    }

    override fun initViews() {
        super.initViews()
        getPokemonSpecies()
    }

    private fun getPokemonSpecies() {
        val id = intent.getIntExtra(KeyConstant.POKEMON_ID, 0)
        val name = intent.getStringExtra(KeyConstant.POKEMON_NAME)
        val image = intent.getStringExtra(KeyConstant.IMAGE)

        if (id == 0 || name == null || image == null) {
            processError("Pokemon ID not found")
            finish()
        } else {
            pokemonSpeciesUseCase(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ pokemonSpecies ->
                    binding.imageViewPokemon.setImageUrl(image)

                    binding.textViewName.text = name
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
}