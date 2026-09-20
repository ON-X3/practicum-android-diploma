package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding

class VacancyFragment : Fragment() {
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModel()
    private var layoutServerError: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutServerError = view.findViewById(R.id.placeholder)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        val vacancyId = arguments?.getString(ARGS_VACANCY_ID).orEmpty()
        viewModel.loadVacancy(vacancyId)

        setListeners()
    }

    private fun render(state: VacancyScreenState) {
        when (state) {
            VacancyScreenState.Loading -> {
                layoutServerError?.isVisible = false
            }
            VacancyScreenState.ServerError -> {
                layoutServerError?.isVisible = true
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        layoutServerError = null
        super.onDestroyView()
    }

    private fun setListeners() {
        binding.apply {
            backButton.setOnClickListener { findNavController().navigateUp() }
        }
    }

    companion object {
        private const val ARGS_VACANCY_ID = "vacancy_id"
        fun createArgs(vacancyId: String): Bundle = Bundle().apply {
            putString(ARGS_VACANCY_ID, vacancyId)
        }
    }
}
