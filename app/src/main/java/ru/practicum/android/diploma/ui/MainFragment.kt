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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentMainBinding
import kotlin.toString
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.presentation.search.SearchState
import ru.practicum.android.diploma.presentation.search.SearchViewModel

class MainFragment : Fragment() {
    private var searchQuery: String = EMPTY_TEXT
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

                val searchIcon = if (hasText) null
                else ContextCompat.getDrawable(requireContext(), R.drawable.ic_search_24)

                binding.searchInputText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    searchIcon,
                    null
                )
                viewModel.searchDebounce(s?.toString().orEmpty())
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
                viewModel.searchDebounce(binding.searchInputText.text.toString())
            }
            false
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    render(state)
                }
            }
        }

    }
    companion object {
        private const val SEARCH_KEY = "search_key"
        private const val EMPTY_TEXT = ""
    }
    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Default -> {
                showPlaceholder(
                    imageRes = R.drawable.il_search,
                    textRes = null
                )
            }
            is SearchState.Loading -> {
                binding.placeholderLayout.isVisible = false
            }
            is SearchState.Content -> {
                binding.placeholderLayout.isVisible = false
            }
            is SearchState.Empty -> {
                showPlaceholder(
                    imageRes = R.drawable.ic_nothing_found,
                    textRes = R.string.nothing_found
                )
            }
            is SearchState.NoInternet -> {
                showPlaceholder(
                    imageRes = R.drawable.ic_no_internet,
                    textRes = R.string.no_internet
                )
            }
            is SearchState.ServerError -> {
                showPlaceholder(
                    imageRes = R.drawable.ic_server_error,
                    textRes = R.string.server_error
                )
            }
        }
    }

    private fun showPlaceholder(imageRes: Int, textRes: Int?) {
        binding.placeholderLayout.isVisible = true
        binding.placeHolderImage.setImageResource(imageRes)

        if (textRes != null) {
            binding.placeholderText.isVisible = true
            binding.placeholderText.setText(textRes)
        } else {
            binding.placeholderText.isVisible = false
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
