package com.example.playlistmaker.search.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.search.Statement
import com.example.playlistmaker.search.domain.Interactor
import kotlinx.coroutines.launch


class SearchViewModel(
    private val interactor: Interactor
) : ViewModel() {

    private val trackMutableLiveData = MutableLiveData<Statement>()
    fun getTracklistLiveData(): LiveData<Statement> = trackMutableLiveData

    init {
        history()
    }

    private var latestString: String? = null

    fun doRequest(expression: String) {
        trackMutableLiveData.postValue(Statement.Loading)

        viewModelScope.launch {
            interactor
                .doRequest(expression)
                .collect { pair ->
                    processResult(pair)
                    Log.i("Flow thing", pair.toString())
                }
        }
    }

    private fun processResult(state: Statement) {
        trackMutableLiveData.postValue(state)
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

    override fun onCleared() {
        super.onCleared()
    }
}