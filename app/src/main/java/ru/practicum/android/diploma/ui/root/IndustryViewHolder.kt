package ru.practicum.android.diploma.ui.root

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Industry

class IndustryViewHolder(itemView: View, private val onClick: (Industry) -> Unit) : RecyclerView.ViewHolder(itemView) {

    private val name: TextView = itemView.findViewById(R.id.nameIndustry)
    private val radioButton: RadioButton = itemView.findViewById(R.id.radioButton)

    fun bind(item: Industry, isSelected: Boolean) {
        name.text = item.industryName
        radioButton.isChecked = isSelected
        itemView.setOnClickListener { onClick(item) }
    }
}

object DiffCallback : DiffUtil.ItemCallback<Industry>() {
    override fun areItemsTheSame(oldItem: Industry, newItem: Industry): Boolean =
        oldItem.industryId == newItem.industryId

    override fun areContentsTheSame(oldItem: Industry, newItem: Industry): Boolean =
        oldItem == newItem
}