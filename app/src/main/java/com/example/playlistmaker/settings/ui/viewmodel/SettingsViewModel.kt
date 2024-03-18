package com.example.playlistmaker.settings.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.Impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor


class SettingsViewModel(private val context: Context) :
    ViewModel() {

    val settingsInteractor: SettingsInteractor = SettingsInteractorImpl(context)

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(context) as T
                }
            }
    }

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
    fun isDarkMode():Boolean{
        return settingsInteractor.isDarkMode()
    }

    override fun onCleared() {
        super.onCleared()
    }


}