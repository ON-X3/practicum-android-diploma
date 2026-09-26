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
import ru.practicum.android.diploma.databinding.FragmentWorkLocationBinding
import ru.practicum.android.diploma.presentation.WorkLocationScreenState
import ru.practicum.android.diploma.presentation.WorkLocationViewModel

class WorkLocationFragment : Fragment() {

    private var _binding: FragmentWorkLocationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkLocationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()

        }

        binding.countryContainer.setOnClickListener {
            findNavController().navigate(R.id.action_workLocationFragment_to_countryFragment)
        }

        binding.regionContainer.setOnClickListener {
            findNavController().navigate(R.id.action_workLocationFragment_to_regionFragment)
        }

        binding.btnSelect.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
    }

    private fun renderState(state: WorkLocationScreenState) {
        if (state.country.isNullOrBlank()) {
            binding.countryPlaceholder.isVisible = true
            binding.countrySelectedContainer.isVisible = false
            binding.countryActionIcon.setImageResource(R.drawable.ic_arrow_forward)
            binding.countryActionIcon.setOnClickListener {
                binding.countryContainer.performClick()
            }
        } else {
            binding.countryPlaceholder.isVisible = false
            binding.countrySelectedContainer.isVisible = true
            binding.countryValue.text = state.country
            binding.countryActionIcon.setImageResource(R.drawable.ic_close_24)
            binding.countryActionIcon.setOnClickListener {
                viewModel.clearCountry()
            }
        }

        if (state.region.isNullOrBlank()) {
            binding.regionPlaceholder.isVisible = true
            binding.regionSelectedContainer.isVisible = false
            binding.regionActionIcon.setImageResource(R.drawable.ic_arrow_forward)
            binding.regionActionIcon.setOnClickListener {
                binding.regionContainer.performClick()
            }
        } else {
            binding.regionPlaceholder.isVisible = false
            binding.regionSelectedContainer.isVisible = true
            binding.regionValue.text = state.region
            binding.regionActionIcon.setImageResource(R.drawable.ic_close_24)
            binding.regionActionIcon.setOnClickListener {
                viewModel.clearRegion()
            }
        }

        binding.btnSelect.isVisible = state.isSelectButtonVisible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
