package com.example.playlistmaker.player

import android.media.MediaPlayer
import com.example.playlistmaker.mediateka.domain.FavoriteRepository
import com.example.playlistmaker.search.data.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val favoriteRepository: FavoriteRepository
) : PlayerRepository {
    override fun setDataSource(url: String) {
        mediaPlayer.setDataSource(url)
    }

    override fun prepareAsync() {
        mediaPlayer.prepareAsync()
    }

    override fun getCurrentPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: "00:00"
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

    override fun isPlayeing(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun isLiked(trackId: Int): Boolean {
//        return when {
//            favoriteRepository.isFavorite(trackId).equals(true) -> {
//                true
//            }
//
//            else -> {
//                false
//            }
//        }
        return true
    }

    override suspend fun likeTrack(track: Track) {
        favoriteRepository.likeTrack(track)
    }

    override suspend fun deleteTrack(trackId: Int) {
        favoriteRepository.deleteTrack(trackId)
    }

    override suspend fun isExists(trackId: Int): Boolean {
        return favoriteRepository.isExists(trackId)
    }

}