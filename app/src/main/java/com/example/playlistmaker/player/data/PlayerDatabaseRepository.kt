package com.example.playlistmaker.player.data

import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.search.data.Track

interface PlayerDatabaseRepository {
    suspend fun likeTrack(track: Track)

    suspend fun deleteTrack(trackId: Int)

    suspend fun isExists(trackId: Int): Boolean

    suspend fun getPlaylists(): List<Playlist>

    suspend fun isTrackExistInPlaylist(playlistId: Int): List<String>

    suspend fun updateTrackList(playlistId: Int, newTrackList: List<String>)

    suspend fun updateNumberOfTrack(newNumber: Int, playlistId: Int)

    suspend fun insertTrackToLocal(track: Track)
}