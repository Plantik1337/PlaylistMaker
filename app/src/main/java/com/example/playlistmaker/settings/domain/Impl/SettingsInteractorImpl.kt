package com.example.playlistmaker.settings.domain.Impl

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl

class SettingsInteractorImpl: SettingsInteractor {

    private val repository: SettingsRepository = SettingsRepositoryImpl()
    override fun shareLink(context: Context) {
        repository.shareLink(context)
    }

    override fun supportContact(context: Context) {
        repository.supportContact(context)
    }

    override fun termOfUse(context: Context) {
        repository.termOfUse(context)
    }
    override fun themeSwitch(value: Boolean, context: Context){
        (context.applicationContext as App).switchTheme(value)
    }

    override fun isDarkMode(context: Context): Boolean {
        return (context.applicationContext as App).isDarkMode()
    }
}