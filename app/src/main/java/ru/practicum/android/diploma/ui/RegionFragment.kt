package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.presentation.RegionState
import ru.practicum.android.diploma.presentation.RegionViewModel

class RegionFragment : Fragment() {
    // Rename and change types of parameters
    private var _binding: FragmentRegionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegionViewModel by viewModel()
    private var _adapter: FilterAreaAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _adapter = FilterAreaAdapter { name ->
            onAreaClick(name)
        }
        binding.areasList.adapter = adapter
        binding.searchInputText.addTextChangedListener { s ->
            val hasText = !s.isNullOrEmpty()
            binding.clearIcon.isVisible = hasText
            viewModel.onSearchTextChanged(s.toString().trim())

            val searchIcon = if (hasText) {
                null
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_search_24)
            }

            binding.searchInputText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                searchIcon,
                null
            )
        }

        viewModel.state().observe(viewLifecycleOwner) {
            when (it) {
                is RegionState.Content -> showContent(it.regions)
                is RegionState.Empty -> showEmptyState()
                is RegionState.Error -> showErrorState()
                is RegionState.Loading -> showLoading()
            }
        }

        binding.clearIcon.setOnClickListener {
            binding.searchInputText.setText("")
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        binding.areasList.adapter = null
        _adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun showContent(regions: List<String>) {
        adapter.addAreas(regions)
        binding.apply {
            placeholder.isVisible = false
            areasList.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun showEmptyState() {
        binding.apply {
            progressBar.isVisible = false
            placeholder.isVisible = true
            areasList.isVisible = false
            placeHolderImage.setImageResource(R.drawable.ic_nothing_found)
            placeholderText.setText(R.string.regions_not_found)
        }
        adapter.clear()
    }

    private fun showErrorState() {
        binding.apply {
            progressBar.isVisible = false
            placeholder.isVisible = true
            areasList.isVisible = false
            placeHolderImage.setImageResource(R.drawable.il_areas_error)
            placeholderText.setText(R.string.regions_error)
        }
        adapter.clear()
    }

    private fun showLoading() {
        binding.apply {
            progressBar.isVisible = true
            placeholder.isVisible = false
            areasList.isVisible = false
        }
    }

    private fun onAreaClick(name: String) {
        viewModel.onAreaClick(name)
        findNavController().popBackStack()
    }

}
