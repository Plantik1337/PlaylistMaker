package com.example.playlistmaker.settings.domain.Impl

import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository): SettingsInteractor {
    override fun shareLink() {
        repository.shareLink()
    }

    override fun supportContact() {
        repository.supportContact()
    }

    override fun termOfUse() {
        repository.termOfUse()
    }
}