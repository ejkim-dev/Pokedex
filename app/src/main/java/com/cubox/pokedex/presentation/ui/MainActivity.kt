package com.cubox.pokedex.presentation.ui

import com.cubox.pokedex.R
import com.cubox.pokedex.databinding.ActivityMainBinding
import com.cubox.pokedex.domain.MyPokemonManager
import com.cubox.pokedex.domain.model.PokemonInfo
import com.cubox.pokedex.domain.usecase.PokemonUseCase
import com.cubox.pokedex.presentation.adapter.MainPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val pokemonUseCase: PokemonUseCase by lazy {
        PokemonUseCase()
    }
    private val pokemonSubject = BehaviorSubject.create<List<PokemonInfo>>()
    private var _isLoading = false
    val isLoading: Boolean
        get() = _isLoading


    override fun initViews() {
        super.initViews()
        setLayoutMargin()
        getPokemon(offset = 0)
        MyPokemonManager.clearMyPokemonHistory()

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
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { _isLoading = true }
            .doOnTerminate { _isLoading = false }
            .subscribe({
                if (pokemonSubject.value == null) {
                    pokemonSubject.onNext(it)
                } else {
                    val currentList = pokemonSubject.value ?: emptyList()
                    pokemonSubject.onNext(currentList + it)
                }
            }, {
                processError(it)
            })
            .addTo(disposables)
    }

    fun observePokemon(): Observable<List<PokemonInfo>> {
        return pokemonSubject.hide()
    }
}