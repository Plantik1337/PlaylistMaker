package com.example.playlistmaker.mediateka.domain.create_playlist

import com.example.playlistmaker.mediateka.data.Playlist

interface CreatePlaylistRepository {
    suspend fun addNewPlaylist(playlist: Playlist)
}