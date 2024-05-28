package com.example.playlistmaker.player.domain

import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.search.data.Track

interface PlayerRepository {
    fun setDataSource(url: String)

    fun prepareAsync()

    fun getCurrentPosition(): String

    fun setOnPreparedListener(onPreparedListener: () -> Unit)

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun start()

    fun pause()

    fun release()

    fun isPlayeing(): Boolean

    suspend fun likeTrack(track: Track)

    suspend fun deleteTrack(trackId: Int)

    suspend fun isExists(trackId: Int): Boolean

    suspend fun getPlaylists(): List<Playlist>

    suspend fun isTrackExistInPlaylist(
        playlistId: Int,
        playlistName: String,
        track: Track
    ): String

    suspend fun insertTrackToLocal(track: Track)
}