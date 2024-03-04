package com.example.playlistmaker.search.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Track
import com.example.playlistmaker.search.Statement
import com.example.playlistmaker.search.domain.Interactor
import com.example.playlistmaker.search.domain.InteractorImlp

class SearchViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    private val interactor: Interactor = InteractorImlp()

    private var trackMutableLiveData = MutableLiveData<Statement>()
    fun getTracklistLiveData(): LiveData<Statement> = trackMutableLiveData

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getViewModelFactory(sharedPreferences: SharedPreferences): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(sharedPreferences) as T
                }
            }
    }

    init {
        history()
    }


    fun doRequest(expression: String) {
        //val newList = Statement.Request(interactor.doRequest(expression))
        Thread {
            val responseData = interactor.doRequest(expression)
            when {
                responseData is Statement.Error -> {
                    trackMutableLiveData.postValue(Statement.Error(responseData.errorMessage))
                }
                responseData is Statement.Success -> {
                    trackMutableLiveData.postValue(Statement.Success(responseData.trackList))
                }
            }
        }.start()

            //trackMutableLiveData.postValue(interactor.doRequest(expression))
    }

    fun history() {
        trackMutableLiveData.postValue(Statement.HISTORY(interactor.read(sharedPreferences)))
    }

    fun clearHistory() {
        interactor.clearHistory(sharedPreferences)
        trackMutableLiveData.postValue(Statement.HISTORY(emptyList()))
    }

    fun writeToHistory(track: Track) {
        interactor.write(sharedPreferences, track)
    }


}