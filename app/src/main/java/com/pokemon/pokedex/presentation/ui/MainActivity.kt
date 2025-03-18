package com.pokemon.pokedex.presentation.ui

import com.pokemon.pokedex.R
import com.pokemon.pokedex.data.repository.PokemonRepository
import com.pokemon.pokedex.databinding.ActivityMainBinding
import com.pokemon.pokedex.domain.entity.PokemonInfo
import com.pokemon.pokedex.domain.usecase.PokemonUseCase
import com.pokemon.pokedex.presentation.adapter.MainPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.pokemon.pokedex.domain.usecase.PokemonInfoUseCase
import com.pokemon.pokedex.domain.usecase.SavePokemonInfoUseCase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val pokemonRepository = PokemonRepository()
    private val pokemonUseCase: PokemonUseCase by lazy { PokemonUseCase(pokemonRepository) }
    private val savePokemonInfoUseCase: SavePokemonInfoUseCase by lazy {
        SavePokemonInfoUseCase(pokemonRepository)
    }
    private val pokemonInfoUseCase: PokemonInfoUseCase by lazy {
        PokemonInfoUseCase(pokemonRepository)
    }
    private val pokemonSubject = BehaviorSubject.create<List<PokemonInfo>>()
    private var _isLoading = false
    val isLoading: Boolean
        get() = _isLoading


    override fun initViews() {
        super.initViews()
        setLayoutMargin()
        getPokemon(offset = 0)

        binding.viewPagerMain.adapter = MainPagerAdapter(this@MainActivity)
    }

    override fun subscribeView() {
        super.subscribeView()

        with(binding) {
            TabLayoutMediator(tabLayoutMain, viewPagerMain) { tab, position ->
                tab.text = when (position) {
                    1 -> getString(R.string.pokemon_list_title)
                    else -> getString(R.string.my_pokemon_title)
                }

                tab.icon = when (position) {
                    1 -> getDrawableId(R.drawable.ic_location_searching_24)
                    else -> getDrawableId(R.drawable.ic_catching_pokemon_24)
                }
            }.attach()
        }
    }

    fun getPokemon(limit: Int = 10, offset: Int) {
        pokemonUseCase(limit, offset)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { _isLoading = true }
            .doOnTerminate { _isLoading = false }
            .map { savePokemonInfoUseCase(it) }
            .subscribe({
                val currentPokemonInfo = pokemonInfoUseCase()
                pokemonSubject.onNext(currentPokemonInfo)
            }, {
                processError(it)
            })
            .addTo(disposables)
    }

    fun observePokemon(): Observable<List<PokemonInfo>> {
        return pokemonSubject.hide()
    }
}