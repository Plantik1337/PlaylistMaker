package com.example.playlistmaker.mediateka.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

class FavoriteViewMoel(private val data: String) : ViewModel() {
    init { // блок init не выводится
        Log.i(
            "FavoriteFragment - ViewModel",
            "FavoriteFragment - ViewModel была создана! Доказательста: 123"
        )

    }

    fun showLog() {
        Log.i(
            "FavoriteFragment - ViewModel",
            "FavoriteFragment - ViewModel была создана! Доказательста: ${data}"
        )
    }

    override fun onCleared() {
        super.onCleared()
    }
}