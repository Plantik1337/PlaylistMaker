package com.example.playlistmaker.player.data

import com.example.playlistmaker.search.data.Track

interface PlayerDatabaseRepository {
    suspend fun likeTrack(track: Track)

    suspend fun deleteTrack(trackId: Int)

    suspend fun isExists(trackId: Int): Boolean
}