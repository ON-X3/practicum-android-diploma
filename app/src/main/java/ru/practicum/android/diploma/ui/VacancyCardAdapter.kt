package ru.practicum.android.diploma.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.models.VacancyCard

class VacancyCardAdapter(val clickListener: VacancyCardClickListener) :
    RecyclerView.Adapter<VacancyCardViewHolder>() {

    private val vacancies: MutableList<VacancyCard> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyCardViewHolder =
        VacancyCardViewHolder.from(parent)

    override fun onBindViewHolder(holder: VacancyCardViewHolder, position: Int) {
        holder.bind(vacancies[position])
        holder.itemView.setOnClickListener { clickListener.onVacancyCardClick(vacancies[position]) }
    }

    override fun getItemCount() = vacancies.size

    fun interface VacancyCardClickListener {
        fun onVacancyCardClick(vacancyCard: VacancyCard)
    }

    fun addVacancies(newVacancies: List<VacancyCard>) {
        val oldSize = itemCount
        vacancies.addAll(newVacancies)
        notifyItemRangeInserted(oldSize, newVacancies.size)
    }

}
