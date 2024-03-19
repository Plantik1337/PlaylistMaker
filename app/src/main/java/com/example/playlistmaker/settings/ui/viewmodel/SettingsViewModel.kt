package com.example.playlistmaker.settings.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.Impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor


class SettingsViewModel(private val settingsInteractor: SettingsInteractor) :
    ViewModel() {

//    val intentLiveData = MutableLiveData<Intent>()
//    fun getLiveData():LiveData<Intent> = intentLiveData

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