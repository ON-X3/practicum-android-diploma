package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.api.SharingRepository

class SharingInteractorImpl(private val sharingRepository: SharingRepository): SharingInteractor {
    override fun shareVacancy(sharingUrl: String) {
        sharingRepository.shareVacancy(sharingUrl)
    }
}
