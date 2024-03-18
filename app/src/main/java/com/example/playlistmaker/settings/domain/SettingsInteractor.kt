package com.example.playlistmaker.settings.domain

import android.content.Context

interface SettingsInteractor {
    fun themeSwitch(value: Boolean, context: Context)
    fun shareLink(context: Context)
    fun supportContact(context: Context)
    fun termOfUse(context: Context)
    fun isDarkMode(context: Context):Boolean
}