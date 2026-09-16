package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentMainBinding
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.presentation.SearchStateUi
import ru.practicum.android.diploma.presentation.SearchViewModel
import kotlin.getValue
import kotlin.toString

class MainFragment : Fragment() {
    private var searchQuery: String = EMPTY_TEXT
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()
    private var _adapter: VacancyCardAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = VacancyCardAdapter { vacancyCard ->
            onVacancyCardClick(vacancyCard)
        }
        binding.vacanciesList.adapter = adapter

        savedInstanceState?.getString(SEARCH_KEY)?.let {
            binding.searchInputText.setText(it)
            searchQuery = it
        }

        binding.clearIcon.setOnClickListener {
            binding.searchInputText.setText("")
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasText = !s.isNullOrEmpty()
                binding.clearIcon.isVisible = hasText
                viewModel.onSearchTextChanged(s.toString())

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

            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString()
            }
        }
        binding.searchInputText.addTextChangedListener(simpleTextWatcher)

        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE &&
                binding.searchInputText.text.isNotEmpty()
            ) {
                // to-do
            }
            false
        }

        viewModel.observeSearchStateUi().observe(viewLifecycleOwner) {
            when (it) {
                is SearchStateUi.Default -> showDefaultState()
                is SearchStateUi.Loading -> showLoadingState()
                is SearchStateUi.Success -> showContent(it.vacancies, it.amountOfVacancies, it.hasNextPage)
                is SearchStateUi.Empty -> showEmpty()
                is SearchStateUi.NoMoreItems -> {}
                is SearchStateUi.Error -> {} //Обработка ошибок из E1T4
            }
        }

        binding.vacanciesList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {

                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int
                ) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        val layoutManager =
                            binding.vacanciesList.layoutManager as LinearLayoutManager

                        val pos = layoutManager.findLastVisibleItemPosition()
                        val itemsCount = adapter.itemCount

                        if (pos >= itemsCount - 1) {
                            viewModel.loadNextPage()
                        }
                    }
                }
            }
        )
    }

    override fun onDestroyView() {
        binding.vacanciesList.adapter = null
        _adapter = null
        _binding = null
        super.onDestroyView()
    }


    //Для всех функций show... добавить изменение видимости вью,
    // отвечающих за обработку различных ошибок (после слияния с E1T4)
    private fun showDefaultState() {
        binding.apply {
            placeHolderImage.setImageResource(R.drawable.il_search)
            placeHolderImage.isVisible = true
            commonProgressBar.isVisible = false
            amountOfVacancies.isVisible = false
            vacanciesList.isVisible = false
        }
        adapter.clear()
    }

    private fun showLoadingState() {
        binding.apply {
            placeHolderImage.isVisible = false
            commonProgressBar.isVisible = true
            amountOfVacancies.isVisible = false
            vacanciesList.isVisible = false
        }
        adapter.clear()
    }

    private fun showContent(vacancies: List<VacancyCard>, amount: Int, hasNextPage: Boolean) {
        adapter.addVacancies(vacancies, hasNextPage)
        binding.apply {
            placeHolderImage.isVisible = false
            commonProgressBar.isVisible = false
            amountOfVacancies.text = getString(R.string.found_some_vacancies, amount)
            amountOfVacancies.isVisible = true
            vacanciesList.isVisible = true
        }
    }

    private fun showEmpty() {
        binding.apply {
            placeHolderImage.isVisible = false
            commonProgressBar.isVisible = false
            amountOfVacancies.text = getString(R.string.vacancies_not_found)
            amountOfVacancies.isVisible = true
            vacanciesList.isVisible = false
        }
        adapter.clear()
    }

    private fun onVacancyCardClick(vacancyCard: VacancyCard) {
        findNavController().navigate(
            R.id.action_mainFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancyCard.id)
        )
    }

    companion object {
        private const val SEARCH_KEY = "search_key"
        private const val EMPTY_TEXT = ""
    }
}
