package com.pokemon.pokedex.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pokemon.pokedex.presentation.ui.MyPokemonFragment
import com.pokemon.pokedex.presentation.ui.PokemonListFragment

class MainPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            1 -> PokemonListFragment()
            else -> MyPokemonFragment()
        }
    }
}