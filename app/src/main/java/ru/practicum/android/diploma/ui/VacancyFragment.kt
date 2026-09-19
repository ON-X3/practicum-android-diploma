package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding

class VacancyFragment : Fragment() {
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
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
        setListeners()
    }
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
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
