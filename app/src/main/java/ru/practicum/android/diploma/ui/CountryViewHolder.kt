package ru.practicum.android.diploma.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.CountryItemBinding
import ru.practicum.android.diploma.domain.models.FilterArea

class CountryViewHolder(private val binding: CountryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: FilterArea) {
        binding.apply {
            countryName.text = model.name
        }
    }
    companion object {
        fun from(parent: ViewGroup): CountryViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = CountryItemBinding.inflate(inflater, parent, false)
            return CountryViewHolder(binding)
        }
    }
}
