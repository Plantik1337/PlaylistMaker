package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.mediateka.data.Playlist

interface PlaylistInteractor {
    suspend fun getPlaylists(): List<Playlist>
}