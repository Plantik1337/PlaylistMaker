package com.example.playlistmaker.settings.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator.provideSettingsInteractor


class SettingsViewModelFactory(var context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            settingsInteractor = provideSettingsInteractor(context = context)
        ) as T

    }
}