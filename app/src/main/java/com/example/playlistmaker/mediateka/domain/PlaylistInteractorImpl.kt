package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.mediateka.data.PlaylistRepository

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun getPlaylists(): List<Playlist> {
        return repository.getPlaylists()
    }

    override suspend fun deletePlaylist(key: Int) {
        repository.deletePlaylist(key)
    }
}