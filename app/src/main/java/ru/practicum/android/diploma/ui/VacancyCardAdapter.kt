package ru.practicum.android.diploma.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.models.VacancyCard

class VacancyCardAdapter(val clickListener: VacancyCardClickListener) :
    RecyclerView.Adapter<VacancyCardViewHolder>() {
    private var hasNextPage: Boolean = false
    private val vacancies = mutableListOf<VacancyCard>()
    private var isLoadingVisible: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyCardViewHolder =
        VacancyCardViewHolder.from(parent)

    override fun onBindViewHolder(holder: VacancyCardViewHolder, position: Int) {
        holder.bind(vacancies[position])
        holder.updateLoadingState(position == vacancies.lastIndex && hasNextPage && isLoadingVisible)
        holder.itemView.setOnClickListener {
            clickListener.onVacancyCardClick(vacancies[position])
        }
    }

    override fun getItemCount() = vacancies.size

    fun interface VacancyCardClickListener {
        fun onVacancyCardClick(vacancyCard: VacancyCard)
    }

    fun addVacancies(newVacancies: List<VacancyCard>, hasNextPage: Boolean) {
        this.hasNextPage = hasNextPage
        vacancies.clear()
        vacancies.addAll(newVacancies)
        notifyDataSetChanged()
    }

    fun onNextPageLoadingError() {
        isLoadingVisible = false
        notifyItemChanged(vacancies.lastIndex)
    }

    fun onNextPageLoading() {
        isLoadingVisible = true
        notifyItemChanged(vacancies.lastIndex)
    }

    fun updateVacancies(newVacancies: List<VacancyCard>) {
        vacancies.clear()
        vacancies.addAll(newVacancies)
        notifyDataSetChanged()
    }

    fun clear() {
        hasNextPage = false
        vacancies.clear()
    }
}
