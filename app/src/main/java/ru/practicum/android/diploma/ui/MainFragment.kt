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
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentMainBinding
import kotlin.toString

class MainFragment : Fragment() {
    private var searchQuery: String = EMPTY_TEXT
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

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

    }
    companion object {
        private const val SEARCH_KEY = "search_key"
        private const val EMPTY_TEXT = ""
    }
}
