package com.example.playlistmaker.search.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.search.Statement
import com.example.playlistmaker.search.domain.Interactor
import com.example.playlistmaker.search.domain.InteractorImlp
import org.koin.java.KoinJavaComponent.inject

class SearchViewModel(
    private val interactor: Interactor
) : ViewModel() {

    private val trackMutableLiveData = MutableLiveData<Statement>()

    fun getTracklistLiveData(): LiveData<Statement> = trackMutableLiveData

    init {
        history()
    }


    fun doRequest(expression: String, context: Context) {
        trackMutableLiveData.postValue(Statement.Loading)
        Thread {
            val responseData = interactor.doRequest(expression, context)
            when {
                responseData is Statement.Error -> {
                    trackMutableLiveData.postValue(Statement.Error(responseData.errorMessage))
                }

                responseData is Statement.Success -> {
                    trackMutableLiveData.postValue(Statement.Success(responseData.trackList))
                }
            }
        }.start()
    }

    fun history() {
        trackMutableLiveData.postValue(Statement.HISTORY(interactor.read()))
    }

    fun clearHistory() {
        interactor.clearHistory()
        trackMutableLiveData.postValue(Statement.HISTORY(emptyList()))
    }

    fun writeToHistory(track: Track) {
        interactor.write(track)
    }
}