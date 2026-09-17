package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R

class VacancyFragment : Fragment() {
    private val viewModel: VacancyViewModel by viewModel()
    private var layoutServerError: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vacancy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutServerError = view.findViewById(R.id.layoutServerError)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        val vacancyId = arguments?.getString(ARGS_VACANCY_ID).orEmpty()
        viewModel.loadVacancy(vacancyId)
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
        super.onDestroyView()
        layoutServerError = null
    }

    companion object {
        private const val ARGS_VACANCY_ID = "vacancy_id"

        fun createArgs(vacancyId: String): Bundle = Bundle().apply {
            putString(ARGS_VACANCY_ID, vacancyId)
        }
    }
}
