package com.example.playlistmaker.settings.domain

interface SettingsInteractor {
    fun themeSwitch(value: Boolean)
    fun shareLink()
    fun supportContact()
    fun termOfUse()
    fun isDarkMode():Boolean
}