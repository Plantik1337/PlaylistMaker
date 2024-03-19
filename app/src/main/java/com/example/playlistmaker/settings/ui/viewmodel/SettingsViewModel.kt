package com.example.playlistmaker.settings.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.Impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor


class SettingsViewModel(private val settingsInteractor: SettingsInteractor) :
    ViewModel() {

    fun share(context: Context) {
        settingsInteractor.shareLink(context)
    }

    fun supportContact(context: Context) {
        settingsInteractor.supportContact(context)
    }

    fun termOfUse(context: Context) {
        settingsInteractor.termOfUse(context)
    }

    fun themeSwitch(value: Boolean, context: Context) {
        settingsInteractor.themeSwitch(value, context)
    }

    fun isDarkMode(context: Context): Boolean {
        return settingsInteractor.isDarkMode(context)
    }

    override fun onCleared() {
        super.onCleared()
    }


}