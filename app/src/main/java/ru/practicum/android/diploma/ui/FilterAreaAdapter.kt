package ru.practicum.android.diploma.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.models.RegionArea

class FilterAreaAdapter(private val clickListener: FilterAreaClickListener) :
    RecyclerView.Adapter<FilterAreaViewHolder>() {

    private val areas: MutableList<RegionArea> = mutableListOf()

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): FilterAreaViewHolder = FilterAreaViewHolder.from(p0)

    override fun onBindViewHolder(holder: FilterAreaViewHolder, position: Int) {
        holder.bind(areas[position])
        holder.itemView.setOnClickListener { clickListener.onFilterAreaClick(areas[position]) }
    }

    override fun getItemCount(): Int {
        return areas.size
    }

    fun addAreas(newAreas: List<RegionArea>) {
        areas.clear()
        areas.addAll(newAreas)
        notifyDataSetChanged()
    }

    fun clear() {
        areas.clear()
    }

    fun interface FilterAreaClickListener {
        fun onFilterAreaClick(area: RegionArea)

    }
}
