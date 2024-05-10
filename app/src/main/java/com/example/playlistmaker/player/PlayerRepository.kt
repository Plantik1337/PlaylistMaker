package com.example.playlistmaker.player

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

    fun isLiked(trackId: Int): Boolean

    suspend fun likeTrack(track: Track)

    suspend fun deleteTrack(trackId: Int)

    suspend fun isExists(trackId: Int): Boolean
}