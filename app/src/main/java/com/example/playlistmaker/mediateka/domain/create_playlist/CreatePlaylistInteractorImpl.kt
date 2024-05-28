package com.example.playlistmaker.mediateka.domain.create_playlist

import com.example.playlistmaker.mediateka.data.Playlist

class CreatePlaylistInteractorImpl(private val createPlaylistRepository: CreatePlaylistRepository):
    CreatePlaylistInteractor {
    override suspend fun addNewPlaylist(playlist: Playlist) {
        createPlaylistRepository.addNewPlaylist(playlist)
    }
}