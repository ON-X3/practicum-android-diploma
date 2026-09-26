package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.RegionArea

sealed class RegionState {
    data class Content(val regions: List<RegionArea>) : RegionState()
    object Empty : RegionState()
    object Error : RegionState()
    object Loading : RegionState()
}
