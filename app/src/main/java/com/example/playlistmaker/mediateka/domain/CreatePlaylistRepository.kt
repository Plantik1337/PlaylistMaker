package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.mediateka.data.Playlist

interface CreatePlaylistRepository {
    suspend fun addNewPlaylist(playlist: Playlist)
}