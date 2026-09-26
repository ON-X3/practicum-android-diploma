package ru.practicum.android.diploma.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyCardItemBinding
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancySalary
import java.util.Currency
import java.util.Locale

class VacancyCardViewHolder(
    private val binding: VacancyCardItemBinding,
    private val onClick: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            val position = bindingAdapterPosition
            onClick(position)
        }
    }

    fun bind(vacancy: VacancyCard) {
        binding.vacancyNameAndCity.text = vacancy.name
        if (!vacancy.city.isNullOrBlank()) {
            binding.vacancyNameAndCity.append(", ${vacancy.city}")
        }

        if (vacancy.company.isNullOrBlank()) {
            binding.company.visibility = View.GONE
        } else {
            binding.company.apply {
                text = vacancy.company
                visibility = View.VISIBLE
            }
        }

        bindSalary(vacancy.salary)

        Glide.with(itemView)
            .load(vacancy.logo)
            .placeholder(R.drawable.logo_placeholder_32)
            .error(R.drawable.logo_placeholder_32)
            .centerInside()
            .into(binding.logo)
    }

    fun updateLoadingState(isLoadingVisible: Boolean) {
        binding.nextPageProgressBar.isVisible = isLoadingVisible
    }

    private fun bindSalary(salary: VacancySalary?) {
        when {
            salary?.from != null && salary.to != null -> {
                binding.salary.text = itemView.resources.getString(
                    R.string.salary_from_to,
                    String.format(Locale.US, "%,d", salary.from).replace(',', ' '),
                    String.format(Locale.US, "%,d", salary.to).replace(',', ' '),
                    Currency.getInstance(salary.currency ?: "RUB").getSymbol(Locale.getDefault())
                )
            }

            salary?.from != null -> {
                binding.salary.text = itemView.resources.getString(
                    R.string.salary_from,
                    String.format(Locale.US, "%,d", salary.from).replace(',', ' '),
                    Currency.getInstance(salary.currency ?: "RUB").getSymbol(Locale.getDefault())
                )
            }

            salary?.to != null -> {
                binding.salary.text = itemView.resources.getString(
                    R.string.salary_to,
                    String.format(Locale.US, "%,d", salary.to).replace(',', ' '),
                    Currency.getInstance(salary.currency ?: "RUB").getSymbol(Locale.getDefault())
                )
            }

            else -> binding.salary.text = itemView.context.resources.getString(R.string.no_salary_info)
        }
    }

    companion object {
        fun from(parent: ViewGroup, onClick: (Int) -> Unit): VacancyCardViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = VacancyCardItemBinding.inflate(inflater, parent, false)
            return VacancyCardViewHolder(binding, onClick)
        }
    }
}
