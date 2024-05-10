package com.example.playlistmaker.mediateka.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.mediateka.domain.FavoriteRepository

class FavoriteViewModel() : ViewModel() {
    init { // блок init не выводится
        Log.i(
            "FavoriteFragment - ViewModel",
            "FavoriteFragment - ViewModel была создана! Доказательста: 123"
        )

    }

    fun showLog() {
        Log.i(
            "FavoriteFragment - ViewModel",
            "FavoriteFragment - ViewModel была создана! Доказательста: 123"
        )
    }

    override fun onCleared() {
        super.onCleared()
    }
}