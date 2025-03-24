package com.pokemon.pokedex.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.pokedex.databinding.ItemPoketmonBinding
import com.pokemon.pokedex.presentation.hide
import com.pokemon.pokedex.presentation.item.BasePokemonItem
import com.pokemon.pokedex.presentation.item.MyPokemonItem
import com.pokemon.pokedex.presentation.item.PokemonItem
import com.pokemon.pokedex.presentation.setImageUrl

class PokemonAdapter(
    private val onItemClick: (BasePokemonItem) -> Unit
) : ListAdapter<BasePokemonItem, PokemonAdapter.ViewHolder>(PokemonDiffUtil) {

    class ViewHolder(
        private val binding: ItemPoketmonBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {
                textViewSavedTime.hide()
                textViewUpdatedTime.hide()
                textViewSavedTimeTitle.hide()
                textViewUpdatedTimeTitle.hide()
            }
        }

        fun bind(item: BasePokemonItem) = with(binding) {
            textViewPokemonName.text = item.name
            imageViewPokemon.setImageUrl(item.imageUrl)
        }

        fun bindMyPokemon(item: BasePokemonItem) = with(binding) {
            textViewPokemonName.text = item.name
            imageViewPokemon.setImageUrl(item.imageUrl)

            textViewSavedTime.text = (item as MyPokemonItem).savedTime
            textViewUpdatedTime.text = item.updatedTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPoketmonBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }

        when (currentItem) {
            is PokemonItem -> holder.bind(currentItem)
            is MyPokemonItem -> holder.bindMyPokemon(currentItem)
        }
    }

    override fun getItemCount(): Int = currentList.size
}