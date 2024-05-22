package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.mediateka.data.Playlist

interface CreatePlaylistInteractor {
    suspend fun addNewPlaylist(playlist: Playlist)
}