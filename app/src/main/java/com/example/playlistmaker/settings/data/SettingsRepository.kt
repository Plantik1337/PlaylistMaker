package com.example.playlistmaker.settings.data

import android.content.Context

interface SettingsRepository {
    fun shareLink(context: Context)
    fun supportContact(context: Context)
    fun termOfUse(context: Context)
}