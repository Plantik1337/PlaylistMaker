package com.example.playlistmaker.mediateka.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.mediateka.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {
    init { // блок init не выводится
    }

    private var playlistMutableLiveData = MutableLiveData<List<Playlist>>()

    fun playlistLiveData(): LiveData<List<Playlist>> = playlistMutableLiveData


    fun getPlaylists(){
        viewModelScope.launch {
            playlistMutableLiveData.postValue(interactor.getPlaylists())
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}