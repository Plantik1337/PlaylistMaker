package com.example.playlistmaker.mediateka.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

class PlaylistViewModel(private val data: String) : ViewModel() {
    var dataViewMoel = data
    init { // блок init не выводится
        dataViewMoel += "123"
        Log.i(
            "PlaylistFragment - ViewModel",
            "PlaylistFragment - ViewModel была создана! Доказательста: 123"
        )
    }
    fun showLog(){
        Log.i(
            "PlaylistFragment - ViewModel",
            "PlaylistFragment - ViewModel была создана! Доказательста: ${dataViewMoel}"
        )
    }

    override fun onCleared() {
        super.onCleared()
    }
}