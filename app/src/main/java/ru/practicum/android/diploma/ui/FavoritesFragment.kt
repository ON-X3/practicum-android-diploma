package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.presentation.FavoritesScreenState
import ru.practicum.android.diploma.presentation.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModel()
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
        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FavoritesScreenState) {
        when (state) {
            is FavoritesScreenState.Loading -> {
                binding.apply {
                    favoritesList.isVisible = false
                    placeholder.isVisible = false
                    progressBar.isVisible = true
                }

            }
            is FavoritesScreenState.Empty -> {
                binding.apply {
                    placeholderImage.setImageResource(R.drawable.il_empty_list)
                    placeholderText.setText(R.string.list_is_empty)
                    favoritesList.isVisible = false
                    placeholder.isVisible = true
                    progressBar.isVisible = false
                }

            }
            is FavoritesScreenState.Content -> {
                addVacanciesToList(state.vacancies)
                binding.apply {
                    favoritesList.isVisible = true
                    binding.placeholder.isVisible = false
                    progressBar.isVisible = false
                }

            }

            is FavoritesScreenState.Error -> {
                binding.apply {
                    placeholderImage.setImageResource(R.drawable.ic_nothing_found)
                    placeholderText.setText(R.string.nothing_found)
                    favoritesList.isVisible = false
                    placeholder.isVisible = true
                    progressBar.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.favoritesList.adapter = null
        _adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun addVacanciesToList(vacancies: List<VacancyCard>) {
        adapter.updateVacancies(vacancies)
    }

    private fun onVacancyCardClick(vacancyCard: VacancyCard) {
        findNavController().navigate(
            R.id.action_favoritesFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancyCard.id)
        )
    }
}
