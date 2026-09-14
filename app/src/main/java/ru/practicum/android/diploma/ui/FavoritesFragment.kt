package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancySalary

class FavoritesFragment : Fragment() {

    private var _adapter: VacancyCardAdapter? = null
    private val adapter get() = _adapter!!

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _adapter = VacancyCardAdapter { vacancyCard ->
            onVacancyCardClick(vacancyCard)
        }
        binding.favoritesList.adapter = adapter

        // Заполнение данными для тестирования, может быть удалено
        addVacanciesToList(
            listOf(
                VacancyCard(
                    "000108c1-f7f5-3241-b9a2-44f6f1a2e58f",
                    "Аналитик данных",
                    "Яндекс",
                    "Челябинск",
                    VacancySalary(null, null, "USD"),
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f1/Yandex_logo_2021_Russian.svg/" +
                        "500px-Yandex_logo_2021_Russian.svg.png"
                ),
                VacancyCard(
                    "00071967-876d-3b5b-bdab-61ad9eab7d05",
                    "Другой аналитик данных",
                    null,
                    "Ижевск",
                    VacancySalary(2500, 4000, "GEL"),
                    "https://upload.wikimediaa.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/" +
                        "500px-Apple_logo_black.svg.png"
                ),
                VacancyCard(
                    "000a6ec6-d5b2-3895-8dd9-55ee911a0e2c",
                    "Вообще совсем другой аналитик данных",
                    "Netflix",
                    null,
                    VacancySalary(1500, null, "EUR"),
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/Netflix_logo.svg/" +
                        "500px-Netflix_logo.svg.png"
                )
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addVacanciesToList(vacancies: List<VacancyCard>) {
        val oldSize = adapter.vacancies.size
        adapter.vacancies.addAll(vacancies)
        adapter.notifyItemRangeInserted(oldSize, vacancies.size)
    }

    private fun onVacancyCardClick(vacancyCard: VacancyCard) {
        findNavController().navigate(
            R.id.action_favoritesFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancyCard.id)
        )
    }
}
