package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.R

class VacancyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vacancy, container, false)
    }

    companion object {
        private const val ARGS_VACANCY_ID = "vacancy_id"

        fun createArgs(vacancyId: String): Bundle = Bundle().apply {
            putString(ARGS_VACANCY_ID, vacancyId)
        }
    }
}
