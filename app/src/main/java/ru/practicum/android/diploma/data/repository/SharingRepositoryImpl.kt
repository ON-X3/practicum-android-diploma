package ru.practicum.android.diploma.data.repository

import android.content.Context
import android.content.Intent
import ru.practicum.android.diploma.domain.api.SharingRepository

class SharingRepositoryImpl(private val context: Context): SharingRepository {
    override fun shareVacancy(sharingUrl: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT, sharingUrl)
        }
        context.startActivity(share)
    }
}
