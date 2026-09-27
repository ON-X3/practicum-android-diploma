package ru.practicum.android.diploma.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.presentation.IndustryState
import ru.practicum.android.diploma.presentation.IndustryViewModel

class IndustryFragment : Fragment() {

    private val viewModel: IndustryViewModel by viewModel()

    private var _binding: FragmentIndustryBinding? = null
    private val binding get() = _binding!!

    private val adapter: IndustryAdapter = IndustryAdapter { industry ->
        viewModel.onIndustrySelected(industry)
        adapter.setSelected(industry.industryId)
        updateButtonVisibility()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupButton()
        setupNavigation()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.industryRecycleView.adapter = null
        _binding = null
    }

    private fun setupRecyclerView() = with(binding) {
        industryRecycleView.layoutManager = LinearLayoutManager(requireContext())
        industryRecycleView.adapter = adapter
    }

    private fun setupSearch() = with(binding) {
        searchInputText.doAfterTextChanged { text ->
            val query = text?.toString().orEmpty()
            val hasText = query.isNotEmpty()

            clearIcon.isVisible = hasText
            updateSearchIcon(hasText)

            viewModel.onSearchQueryChanged(query)
        }

        searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onSearchSubmitted()
                true
            } else {
                false
            }
        }

        clearIcon.setOnClickListener {
            searchInputText.text?.clear()
            hideKeyboard()
        }
    }

    private fun updateSearchIcon(hasText: Boolean) = with(binding) {
        val searchIcon = if (hasText) {
            null
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_search_24)
        }
        searchInputText.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            searchIcon,
            null
        )
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchInputText.windowToken, 0)
    }

    private fun setupButton() = with(binding) {
        btnChoice.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupNavigation() = with(binding) {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::render)
            }
        }
    }

    private fun render(state: IndustryState) = with(binding) {
        progressBar.isVisible = false
        industryRecycleView.isVisible = false
        serverErrorLinear.isVisible = false
        notFoundErrorLinear.isVisible = false

        when (state) {
            IndustryState.Loading -> progressBar.isVisible = true
            is IndustryState.Content -> {
                industryRecycleView.isVisible = true
                adapter.submitList(state.industries)
                adapter.setSelected(viewModel.selectedId)
                updateButtonVisibility()
            }

            IndustryState.Empty -> notFoundErrorLinear.isVisible = true
            IndustryState.Error -> serverErrorLinear.isVisible = true
        }
    }

    private fun updateButtonVisibility() {
        binding.btnChoice.isVisible = viewModel.selectedId != null
    }
}
