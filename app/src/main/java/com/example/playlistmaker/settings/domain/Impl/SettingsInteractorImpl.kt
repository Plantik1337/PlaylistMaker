package com.example.playlistmaker.settings.domain.Impl

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.data.SettingsRepository

class SettingsInteractorImpl(
    private val context: Context,
    private val repository: SettingsRepository
) : SettingsInteractor {

    override fun shareLink() {
        repository.shareLink()
    }

    override fun supportContact() {
        repository.supportContact()
    }

    override fun termOfUse() {
        repository.termOfUse()
    }

    override fun themeSwitch(value: Boolean) {
        (context.applicationContext as App).switchTheme(value)
    }

    override fun isDarkMode(): Boolean {
        return (context.applicationContext as App).isDarkMode()
    }
}