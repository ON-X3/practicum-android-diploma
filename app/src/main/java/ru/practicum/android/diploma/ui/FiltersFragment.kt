package ru.practicum.android.diploma.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.domain.models.FilterAreaDetails
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.FiltersViewModel

class FiltersFragment : Fragment() {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!
    private var salaryRequest: String = EMPTY_TEXT
    private val viewModel: FiltersViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFiltersBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filtersStateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        setListeners()
        backButtonPress()
        val simpleTextWatcher = object : TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when {
                    binding.salaryInputText.hasFocus() -> binding.salaryLabel.setTextColor(
                        resources.getColor(R.color.blue)
                    )

                    s.isNullOrEmpty() -> binding.salaryLabel.setTextColor(resources.getColor(R.color.salaryLabelColor))
                    else -> binding.salaryLabel.setTextColor(resources.getColor(R.color.uniBlack))
                }
                binding.clearSalary.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating || s == null) return

                val text = s.toString()

                if (text.length > 1 && text.startsWith("0")) {
                    isUpdating = true

                    s.delete(0, 1)

                    isUpdating = false
                }
                salaryRequest = s.toString()
                val salarySum = salaryRequest.toIntOrNull()
                viewModel.onSalaryChanged(salarySum)
            }
        }
        binding.salaryInputText.addTextChangedListener(simpleTextWatcher)

        binding.clearSalary.setOnClickListener {
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.salaryInputText.windowToken, 0)
            binding.salaryInputText.setText(EMPTY_TEXT)
            viewModel.clearSalary()
        }
    }

    private fun setListeners() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.onApplyFiltersClick()
            findNavController().popBackStack()
        }

        binding.filterArea.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_workLocationFragment)
        }

        binding.filterIndustry.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_industryFragment)
        }

        binding.hideWithoutSalary.setOnClickListener {
            val hideWOSalary = viewModel.filtersStateLiveData.value?.onlyWithSalary ?: false
            viewModel.updateWithSalary(!hideWOSalary)

        }
        binding.applyFilters.setOnClickListener {
            viewModel.onApplyFiltersClick()

            findNavController().apply {
                previousBackStackEntry?.savedStateHandle?.set(
                    MainFragment.SHOULD_UPDATE_RESULTS_WITH_NEW_FILTER_KEY,
                    true
                )
                popBackStack()
            }
        }

        binding.dropFilters.setOnClickListener {
            viewModel.onDropFiltersClick()
            findNavController().apply {
                previousBackStackEntry?.savedStateHandle?.set(
                    MainFragment.SHOULD_UPDATE_RESULTS_WITH_NEW_FILTER_KEY,
                    true
                )
                popBackStack()
            }
        }

        binding.clearArea.setOnClickListener {
            viewModel.clearArea()
        }
        binding.clearIndustry.setOnClickListener {
            viewModel.clearIndustry()
        }
    }

    private fun updateCheckBoxIcon(setValue: Boolean) {
        val checkBoxIcon = if (setValue) {
            R.drawable.ic_check_box_on_24
        } else {
            R.drawable.ic_check_box_off_24
        }
        binding.hideWithoutSalary.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            checkBoxIcon,
            0,
        )
    }

    private fun render(state: FiltersViewModel.FiltersState) {
        renderArea(state.area)
        renderIndustry(state.industry)
        renderSalary(state.salary)
        updateCheckBoxIcon(state.onlyWithSalary)
        val isFilterNotEmpty = state.area != null
            || state.industry != null
            || state.salary != null
            || state.onlyWithSalary
        if (isFilterNotEmpty) {
            binding.applyFilters.visibility = View.VISIBLE
            binding.dropFilters.visibility = View.VISIBLE
        } else {
            binding.applyFilters.visibility = View.GONE
            binding.dropFilters.visibility = View.GONE
        }
    }

    private fun renderArea(area: FilterAreaDetails?) {
        if (area != null) {
            binding.apply {
                areaValue.text = buildString {
                    append(area.country?.countryName)
                    if (area.region != null) {
                        append(", ${area.region.regionName}")
                    }
                }
                areaLabel.setText(R.string.area_filter)
                filterArea.text = EMPTY_TEXT
                clearArea.isVisible = true
                filterArea.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
            }
        } else {
            binding.apply {
                filterArea.setText(R.string.area_filter)
                areaValue.text = EMPTY_TEXT
                areaLabel.text = EMPTY_TEXT
                clearArea.isVisible = false
                filterArea.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_arrow_forward_24),
                    null
                )
            }

        }
    }

    private fun renderIndustry(industry: Industry?) {
        if (industry == null) {
            binding.apply {
                filterIndustry.setText(R.string.industry_filter)
                industryLabel.text = EMPTY_TEXT
                industryValue.text = EMPTY_TEXT
                clearIndustry.isVisible = false
                filterIndustry.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_arrow_forward_24),
                    null
                )
            }
        } else {
            binding.apply {
                filterIndustry.text = EMPTY_TEXT
                industryLabel.setText(R.string.industry_filter)
                industryValue.text = industry.industryName
                clearIndustry.isVisible = true
                filterIndustry.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
            }
        }
    }

    private fun renderSalary(salary: Int?) {
        if (salary == null) {
            binding.salaryInputText.setText("")
        } else {
            binding.salaryInputText.setText(salary.toString())
        }
        binding.salaryInputText.setSelection(binding.salaryInputText.text.length)
    }

    private fun backButtonPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onApplyFiltersClick()
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val EMPTY_TEXT = ""
    }
}
