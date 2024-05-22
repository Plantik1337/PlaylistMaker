package com.example.playlistmaker.mediateka.data

interface PlaylistRepository {
    suspend fun getPlaylists(): List<Playlist>
}