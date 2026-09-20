package ru.practicum.android.diploma.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.get
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentMainBinding
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.presentation.SearchStateUi
import ru.practicum.android.diploma.presentation.SearchViewModel

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
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputMethodManager =
            requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

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
            inputMethodManager?.hideSoftInputFromWindow(binding.searchInputText.windowToken, 0)
        }

        binding.searchInputText.addTextChangedListener(getSearchTextWatcher())

        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE &&
                binding.searchInputText.text.isNotEmpty()
            ) {
                viewModel.searchWithoutDebounce(binding.searchInputText.text.toString())
            }
            false
        }

        setupObservers()

        binding.addFilter.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_filtersFragment)
        }

        binding.vacanciesList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {

                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int
                ) {
                    super.onScrolled(recyclerView, dx, dy)
                    onRVScrolled(dy)
                }
            }
        )

        val inter: SearchInteractor = get()
        viewLifecycleOwner.lifecycleScope.launch {
            val details = inter.getVacancyDetail("0008f0fb-491f-3789-8de1-49cb412b1f2a")
            Log.d("detail", details.data.toString())
        }
    }

    override fun onDestroyView() {
        binding.vacanciesList.adapter = null
        _adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun setupObservers() {
        viewModel.observeSearchStateUi().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeIsFilterActive().observe(viewLifecycleOwner) {
            renderFilter(it)
        }

        viewModel.errorToast().observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun getSearchTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // not used
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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

        override fun afterTextChanged(s: Editable?) {
            searchQuery = s.toString()
        }
    }

    private fun onRVScrolled(dy: Int) {
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

    private fun renderFilter(isActive: Boolean) {
        if (isActive) {
            binding.addFilter.apply {
                imageTintList = ColorStateList.valueOf(requireContext().getColor(R.color.uniWhite))
                setBackgroundResource(R.drawable.active_filter_background)
            }
        } else {
            binding.addFilter.apply {
                imageTintList = ColorStateList.valueOf(requireContext().getColor(R.color.ypBlack))
                background = null
            }
        }
    }

    private fun showToast(error: ErrorCode) {
        adapter.onNextPageLoadingError()
        if (error == ErrorCode.NO_INTERNET_CONNECTION) {
            Toast.makeText(
                requireContext(),
                R.string.no_internet_toast_message,
                Toast.LENGTH_LONG
            )
                .show()
        } else {
            Toast.makeText(
                requireContext(),
                R.string.error_toast_message,
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun showDefaultState() {
        binding.amountOfVacancies.isVisible = false
        showPlaceholder(
            imageRes = R.drawable.il_search,
            textRes = null
        )
        adapter.clear()
    }

    private fun showLoadingState() {
        binding.apply {
            placeholderLayout.isVisible = false
            commonProgressBar.isVisible = true
            amountOfVacancies.isVisible = false
            vacanciesList.isVisible = false
        }
        adapter.clear()
    }

    private fun showContent(vacancies: List<VacancyCard>, amount: Int, hasNextPage: Boolean) {
        adapter.addVacancies(vacancies, hasNextPage)
        binding.apply {
            placeholderLayout.isVisible = false
            commonProgressBar.isVisible = false
            amountOfVacancies.text = getString(R.string.found_some_vacancies, amount)
            amountOfVacancies.isVisible = true
            vacanciesList.isVisible = true
        }
    }

    private fun showEmpty() {
        binding.amountOfVacancies.isVisible = true
        binding.amountOfVacancies.setText(R.string.vacancies_not_found)
        showPlaceholder(
            imageRes = R.drawable.ic_nothing_found,
            textRes = R.string.nothing_found
        )
        adapter.clear()
    }

    private fun showNoInternetError() {
        binding.amountOfVacancies.isVisible = false
        showPlaceholder(
            imageRes = R.drawable.ic_no_internet,
            textRes = R.string.no_internet
        )
        adapter.clear()
    }

    private fun showServerError() {
        binding.amountOfVacancies.isVisible = false
        showPlaceholder(
            imageRes = R.drawable.ic_server_error,
            textRes = R.string.server_error
        )
        adapter.clear()
    }

    private fun onVacancyCardClick(vacancyCard: VacancyCard) {
        findNavController().navigate(
            R.id.action_mainFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancyCard.id)
        )
    }

    private fun render(state: SearchStateUi) {
        if (!(state is SearchStateUi.Success || state is SearchStateUi.NextPageLoading)) {
            binding.vacanciesList.scrollToPosition(0)
        }
        when (state) {
            is SearchStateUi.Default -> showDefaultState()
            is SearchStateUi.Loading -> showLoadingState()
            is SearchStateUi.NextPageLoading -> adapter.onNextPageLoading()
            is SearchStateUi.Success -> showContent(state.vacancies, state.amountOfVacancies, state.hasNextPage)

            is SearchStateUi.Empty -> showEmpty()

            is SearchStateUi.NoInternetError -> showNoInternetError()

            is SearchStateUi.ServerError -> showServerError()
        }
    }

    private fun showPlaceholder(imageRes: Int, textRes: Int?) {
        binding.apply {
            placeholderLayout.isVisible = true
            placeHolderImage.setImageResource(imageRes)
            commonProgressBar.isVisible = false
            vacanciesList.isVisible = false
        }

        if (textRes != null) {
            binding.placeholderText.isVisible = true
            binding.placeholderText.setText(textRes)
        } else {
            binding.placeholderText.isVisible = false
        }
    }

    companion object {
        private const val SEARCH_KEY = "search_key"
        private const val EMPTY_TEXT = ""
    }
}
