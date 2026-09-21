package ru.practicum.android.diploma.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.models.FilterArea

class CountryAdapter(
    private var countriesList: List<FilterArea>,
    private val onItemClick: ((FilterArea) -> Unit)? = null
) : RecyclerView.Adapter<CountryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder =
        CountryViewHolder.from(parent)
    override fun onBindViewHolder(
        holder: CountryViewHolder,
        position: Int
    ) {
        holder.bind(countriesList[position])
        
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(countriesList[position])
        }
    }
    override fun getItemCount(): Int {
        return countriesList.size
    }
    fun updateCountriesList(newList: List<FilterArea>) {
        this.countriesList = newList
        notifyDataSetChanged()
    }
}
