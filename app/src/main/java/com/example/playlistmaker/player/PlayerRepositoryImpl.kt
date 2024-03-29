package com.example.playlistmaker.player

import android.media.MediaPlayer

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer): PlayerRepository {
    override fun setDataSource(url: String) {
        mediaPlayer.setDataSource(url)
    }

    override fun prepareAsync() {
        mediaPlayer.prepareAsync()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setOnPreparedListener(onPreparedListener: () -> Unit) {
        mediaPlayer.setOnPreparedListener {
            onPreparedListener()
        }
    }

    override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onCompletionListener()
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

}