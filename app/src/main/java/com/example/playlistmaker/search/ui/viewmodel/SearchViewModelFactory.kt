package com.example.playlistmaker.search.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModelFactory(var context: Context):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return super.create(modelClass)
    }
}