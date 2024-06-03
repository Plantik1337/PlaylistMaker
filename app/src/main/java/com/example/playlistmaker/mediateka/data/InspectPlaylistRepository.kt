package com.example.playlistmaker.mediateka.data

import com.example.playlistmaker.mediateka.data.dataBase.PlaylistEntity
import com.example.playlistmaker.search.data.Track

interface InspectPlaylistRepository {
    suspend fun deletePlaylist(key: Int)

    suspend fun deleteTrack(id: Int, playlistKey: Int)

    suspend fun getTrackList(trackList: List<String>): List<Track>

    suspend fun updateNumberofPlaylist(number: Int, playlistKey: Int)

    suspend fun getPlaylist(playlistKey: Int): Playlist
}