package com.example.playlistmaker.settings.domain

import android.content.Context

interface SettingsInteractor {
    fun themeSwitch(value: Boolean)
    fun shareLink()
    fun supportContact()
    fun termOfUse()
    fun isDarkMode():Boolean
}