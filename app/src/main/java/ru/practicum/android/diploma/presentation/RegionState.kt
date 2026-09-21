package ru.practicum.android.diploma.presentation

sealed class RegionState {
    data class Content(val regions: List<String>) : RegionState()
    object Empty : RegionState()
    object Error : RegionState()
    object Loading : RegionState()
}
