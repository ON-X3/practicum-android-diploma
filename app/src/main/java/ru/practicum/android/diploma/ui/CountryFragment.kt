package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.presentation.CountryStateUi
import ru.practicum.android.diploma.presentation.CountryViewModel

class CountryFragment : Fragment() {
    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private lateinit var countryAdapter: CountryAdapter
    private val viewModel: CountryViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
        setListeners()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.countriesRecyclerView.adapter = null
        _binding = null
    }
    private fun initFragment() {
        countryAdapter = CountryAdapter(emptyList(), onItemClick = { currentCountry ->
            viewModel.saveCountry(currentCountry)
            findNavController().popBackStack()
        })
        binding.countriesRecyclerView.adapter = countryAdapter
        viewModel.observeCountryStateUi().observe(viewLifecycleOwner) {
            renderUiState(it)
        }
    }
    private fun setListeners(){
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }
    private fun renderUiState(state: CountryStateUi) {
        when (state) {
            is CountryStateUi.Loading -> showLoading()
            is CountryStateUi.Error -> showError()
            is CountryStateUi.Content -> showContent(state.countries)
        }
    }
    private fun showLoading(){
        binding.apply {
            progressBar.isVisible = true
            countriesRecyclerView.isVisible = false
            countriesError.isVisible = false
        }
    }
    private fun showError(){
        binding.apply {
            progressBar.isVisible = false
            countriesRecyclerView.isVisible = false
            countriesError.isVisible = true
        }
    }
    private fun showContent(countries: List<FilterArea>){
        binding.apply {
            countryAdapter.updateCountriesList(countries)
            progressBar.isVisible = false
            countriesRecyclerView.isVisible = true
            countriesError.isVisible = false
        }
    }
}
