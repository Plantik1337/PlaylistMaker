package com.example.playlistmaker.mediateka.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.mediateka.domain.create_playlist.CreatePlaylistInteractor

class CreatePlaylistViewModel(private val interactor: CreatePlaylistInteractor): ViewModel() {
    suspend fun createPlaylist(playlist: Playlist){
        interactor.addNewPlaylist(playlist)
    }
}