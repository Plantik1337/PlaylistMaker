package com.example.playlistmaker.search.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.Track
import com.example.playlistmaker.search.domain.Interactor
import com.example.playlistmaker.search.domain.InteractorImlp

class SearchViewModel : ViewModel() {

    private val interactor: Interactor = InteractorImlp()

    private var trackMutableLiveData = MutableLiveData<List<Track>>()

    fun getLiveData(): LiveData<List<Track>> = trackMutableLiveData

    fun doRequest(expression: String) {
        trackMutableLiveData.value =
            interactor.doRequest(expression)
    }

    fun history(sharedPreferences: SharedPreferences) {
        trackMutableLiveData.value =
            interactor.read(sharedPreferences)
    }

    fun clearHistory(sharedPreferences: SharedPreferences) {
        interactor.clearHistory(sharedPreferences)
    }

    fun writeToHistory(sharedPreferences: SharedPreferences, track: Track) {
        interactor.write(sharedPreferences, track)
    }
}