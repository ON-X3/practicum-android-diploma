package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.models.VacancySalary
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.presentation.VacancyDetailState
import ru.practicum.android.diploma.presentation.VacancyDetailViewModel
import java.util.Currency
import java.util.Locale

class VacancyFragment : Fragment() {
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private var _viewModel: VacancyDetailViewModel? = null
    private val viewModel get() = _viewModel!!
    private val adapter = SkillAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = getViewModel { parametersOf(requireArguments().getString(ARGS_VACANCY_ID)) }
    }

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
        binding.skillsList.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        setListeners()
    }

    private fun render(state: VacancyDetailState) {
        when (state) {
            is VacancyDetailState.Content -> showContent(state.vacancy)
            is VacancyDetailState.Loading -> showLoading()

            is VacancyDetailState.Error -> showPlaceholder(state.errorCode)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setListeners() {
        binding.apply {
            backButton.setOnClickListener { findNavController().navigateUp() }
            sharingButton.setOnClickListener {
                viewModel.shareVacancy()
            }
            favoriteButton.setOnClickListener {
                viewModel.onFavoriteButtonClick()
            }
        }
    }

    private fun showPlaceholder(error: ErrorCode) {
        when (error) {
            ErrorCode.NOT_FOUND -> {
                binding.apply {
                    placeholderImage.setImageResource(R.drawable.il_404_not_found)
                    placeholderText.setText(R.string.not_found_404_placeholder_text)
                }
            }

            ErrorCode.NO_INTERNET_CONNECTION -> {
                binding.apply {
                    placeholderImage.setImageResource(R.drawable.ic_no_internet)
                    placeholderText.setText(R.string.no_internet)
                }
            }

            else -> {
                binding.apply {
                    placeholderImage.setImageResource(R.drawable.ic_server_error_vc)
                    placeholderText.setText(R.string.server_error)
                }
            }
        }
        binding.apply {
            content.isVisible = false
            placeholder.isVisible = true
            progressBar.isVisible = false
        }

    }

    private fun showLoading() {
        binding.apply {
            content.isVisible = false
            placeholder.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showContent(vacancy: VacancyDetail) {
        Glide.with(this)
            .load(vacancy.employer.logo)
            .placeholder(R.drawable.logo_placeholder_32)
            .error(R.drawable.logo_placeholder_32)
            .centerInside()
            .into(binding.logo)

        bindSkills(vacancy.skills)
        bindSalary(vacancy.salary)
        bindExperience(vacancy.experience)
        bindEmploymentAndSchedule(vacancy.employment, vacancy.schedule)

        binding.apply {
            vacancyName.text = vacancy.name
            description.text = HtmlFormatter(requireContext()).format(vacancy.descriptionHtml)
            address.text = vacancy.address
            companyName.text = vacancy.employer.name
            content.isVisible = true
            placeholder.isVisible = false
            progressBar.isVisible = false
        }

        if (vacancy.isFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.ic_favorites_active_24)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.ic_favorites_non_active_24)
        }
    }

    private fun bindSkills(skills: List<String>) {
        if (skills.isNotEmpty()) {
            adapter.addSkills(skills)
            binding.apply {
                skillsHeader.isVisible = true
                skillsList.isVisible = true
            }
        } else {
            binding.apply {
                skillsHeader.isVisible = false
                skillsList.isVisible = false
            }
        }
    }

    private fun bindSalary(salary: VacancySalary?) {
        if (salary?.from != null || salary?.to != null) {
            binding.salary.text = buildString {
                salary.from?.let {
                    append("от ${String.format(Locale.US, "%,d", it).replace(',', ' ')} ")
                }
                salary.to?.let {
                    append("до ${String.format(Locale.US, "%,d", it).replace(',', ' ')} ")
                }
                salary.currency?.let {
                    append(Currency.getInstance(it).getSymbol(Locale.getDefault()))
                }
            }.trim()
        } else {
            binding.salary.setText(R.string.no_salary_info)
        }
    }

    private fun bindExperience(exp: String?) {
        if (exp.isNullOrBlank()) {
            binding.apply {
                requiredExperience.isVisible = false
                experience.isVisible = false
            }
        } else {
            binding.apply {
                experience.text = exp
                experience.isVisible = true
                requiredExperience.isVisible = true
            }
        }
    }

    fun bindEmploymentAndSchedule(employment: String?, schedule: String?) {
        if (employment.isNullOrBlank() && schedule.isNullOrBlank()) {
            binding.employmentAndSchedule.isVisible = false
            return
        }
        binding.apply {
            employmentAndSchedule.text = buildString {
                if (!employment.isNullOrBlank()) {
                    append(employment)
                }
                if (toString().isNotEmpty() && !schedule.isNullOrBlank()) {
                    append(", ")
                }
                if (!schedule.isNullOrBlank()) {
                    append(schedule)
                }
            }
            employmentAndSchedule.isVisible = true
        }

    }

    companion object {
        private const val ARGS_VACANCY_ID = "vacancy_id"
        fun createArgs(vacancyId: String): Bundle = Bundle().apply {
            putString(ARGS_VACANCY_ID, vacancyId)
        }
    }
}
