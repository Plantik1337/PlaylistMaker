package com.example.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor


class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun share() {
        settingsInteractor.shareLink()
    }

    fun supportContact() {
        settingsInteractor.supportContact()
    }

    fun termOfUse() {
        settingsInteractor.termOfUse()
    }

    override fun onCleared() {
        super.onCleared()
    }


}