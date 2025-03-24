package com.pokemon.pokedex.presentation.ui

import android.annotation.SuppressLint
import com.pokemon.pokedex.R
import com.pokemon.pokedex.databinding.ActivityMainBinding
import com.pokemon.pokedex.presentation.adapter.MainPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initView() {
        with(binding) {
            viewPagerMain.adapter = MainPagerAdapter(this@MainActivity)
            TabLayoutMediator(tabLayoutMain, viewPagerMain) { tab, position ->
                tab.text = when (position) {
                    1 -> getString(R.string.pokemon_list_title)
                    else -> getString(R.string.my_pokemon_title)
                }

                tab.icon = when (position) {
                    1 -> getDrawable(R.drawable.ic_location_searching_24)
                    else -> getDrawable(R.drawable.ic_catching_pokemon_24)
                }
            }.attach()
        }
    }
}