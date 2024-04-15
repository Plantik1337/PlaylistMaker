package com.example.playlistmaker.player

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
}