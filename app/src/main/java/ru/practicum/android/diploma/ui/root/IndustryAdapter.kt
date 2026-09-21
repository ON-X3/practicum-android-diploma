package ru.practicum.android.diploma.ui.root

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Industry


class IndustryAdapter(
    private val onClick: (Industry) -> Unit
) : ListAdapter<Industry, IndustryViewHolder>(DiffCallback) {

    private var selectedId: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.industry_item, parent, false)
        return IndustryViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, item.industryId == selectedId)
    }

    fun setSelected(newId: Int?) {
        if (selectedId == newId) return

        val oldId = selectedId
        selectedId = newId

        currentList.indexOfFirst { it.industryId == oldId }
            .takeIf { it >= 0 }
            ?.let(::notifyItemChanged)

        currentList.indexOfFirst { it.industryId == newId }
            .takeIf { it >= 0 }
            ?.let(::notifyItemChanged)
    }
}
