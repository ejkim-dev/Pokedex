package com.cubox.pokedex.presentation.ui

import com.cubox.pokedex.databinding.ActivityDetailBinding
import com.cubox.pokedex.domain.usecase.PokemonSpeciesUseCase
import com.cubox.pokedex.presentation.KeyConstant
import com.cubox.pokedex.presentation.setImageUrl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo

class DetailActivity : BaseActivity<ActivityDetailBinding>(ActivityDetailBinding::inflate) {

    private val pokemonSpeciesUseCase: PokemonSpeciesUseCase by lazy {
        PokemonSpeciesUseCase()
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