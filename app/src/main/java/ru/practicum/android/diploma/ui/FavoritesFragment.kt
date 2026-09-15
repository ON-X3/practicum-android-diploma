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

    }

    override fun onDestroyView() {
        binding.favoritesList.adapter = null
        _adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun addVacanciesToList(vacancies: List<VacancyCard>) {
        adapter.addVacancies(vacancies)
    }

    private fun onVacancyCardClick(vacancyCard: VacancyCard) {
        findNavController().navigate(
            R.id.action_favoritesFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancyCard.id)
        )
    }
}
