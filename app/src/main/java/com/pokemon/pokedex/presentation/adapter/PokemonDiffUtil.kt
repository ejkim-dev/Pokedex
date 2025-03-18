package com.pokemon.pokedex.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pokemon.pokedex.presentation.item.BasePokemonItem

object PokemonDiffUtil: DiffUtil.ItemCallback<BasePokemonItem>() {
    override fun areItemsTheSame(oldItem: BasePokemonItem, newItem: BasePokemonItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BasePokemonItem, newItem: BasePokemonItem): Boolean {
        return oldItem == newItem
    }
}