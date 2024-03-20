package com.example.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor


class SettingsViewModel(private val settingsInteractor: SettingsInteractor) :
    ViewModel() {

    fun share() {
        settingsInteractor.shareLink()
    }

    fun supportContact() {
        settingsInteractor.supportContact()
    }

    fun termOfUse() {
        settingsInteractor.termOfUse()
    }

    fun themeSwitch(value: Boolean) {
        settingsInteractor.themeSwitch(value)
    }

    fun isDarkMode(): Boolean {
        return settingsInteractor.isDarkMode()
    }

    override fun onCleared() {
        super.onCleared()
    }


}