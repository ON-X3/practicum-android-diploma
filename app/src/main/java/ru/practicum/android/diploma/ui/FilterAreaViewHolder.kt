package ru.practicum.android.diploma.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.FilterAreaItemBinding

class FilterAreaViewHolder(private val binding: FilterAreaItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(name: String) {
        binding.name.text = name
    }

    companion object {
        fun from(parent: ViewGroup): FilterAreaViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = FilterAreaItemBinding.inflate(inflater, parent, false)
            return FilterAreaViewHolder(binding)
        }
    }
}
