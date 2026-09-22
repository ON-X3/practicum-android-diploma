package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import ru.practicum.android.diploma.R
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding

class FiltersFragment : Fragment() {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!
    private var salaryRequest: String = EMPTY_TEXT
    private var hideWOSalary: Boolean = false
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

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.filterArea.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_workLocationFragment)
        }

        binding.filterIndustry.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_industryFragment)
        }

        viewModel.filtersStateLiveData.observe(viewLifecycleOwner){
            render(it)
        }
        binding.hideWithoutSalary.setOnClickListener {
            if (!hideWOSalary) {
                hideWOSalary = true
                viewModel.updateWithSalary(hideWOSalary)
            } else {
                hideWOSalary = false
                viewModel.updateWithSalary(hideWOSalary)
            }
            updateCheckBoxIcon(hideWOSalary)
        }
        binding.applyFilters.setOnClickListener {
            findNavController().navigate(
                R.id.action_filtersFragment_to_mainFragment
            )
        }

        binding.dropFilters.setOnClickListener {
            findNavController().navigateUp()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.salaryInputText.setOnFocusChangeListener { _, hasFocus ->
                        var color = 0
                        if (hasFocus) {
                            color = ContextCompat.getColor(requireContext(), R.color.blue)
                            binding.salaryLabel.setTextColor(color)
                        }
                    }
                    binding.applyFilters.visibility = View.GONE
                    binding.dropFilters.visibility = View.GONE
                } else {
                    binding.applyFilters.visibility = View.VISIBLE
                    binding.dropFilters.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                salaryRequest = s.toString().orEmpty()
                val salarySum = salaryRequest.toIntOrNull()
                viewModel.updateSalary(salarySum)
            }
        }
        binding.salaryInputText.addTextChangedListener(simpleTextWatcher)
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
    private fun render(state: FiltersViewModel.FiltersState){
        val area = state.area
        val industry = state.industry

        if (area != null ) {
            val areaFilter = "${area.country}, ${area.region}"
            binding.filterArea.setText(areaFilter)
        }
        if (industry != null ) {
            binding.filterArea.setText(industry.industryName)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val EMPTY_TEXT = ""
    }
}
