package com.example.playlistmaker.player.domain

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.player.data.PlayerDatabaseRepository
import com.example.playlistmaker.search.data.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val playerDatabaseRepository: PlayerDatabaseRepository
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

    override suspend fun likeTrack(track: Track) {
        playerDatabaseRepository.likeTrack(track)
    }

    override suspend fun deleteTrack(trackId: Int) {
        playerDatabaseRepository.deleteTrack(trackId)
    }

    override suspend fun isExists(trackId: Int): Boolean {
        val tmp = playerDatabaseRepository.isExists(trackId)
        Log.e("Репозиторий", "${tmp}")
        return tmp
    }

    override suspend fun getPlaylists(): List<Playlist> {
        return playerDatabaseRepository.getPlaylists()
    }

    override suspend fun isTrackExistInPlaylist(
        playlistId: Int,
        playlistName: String,
        trackId: String
    ): String {
        var isExists = false
        val playlistTrackIds = playerDatabaseRepository.isTrackExistInPlaylist(playlistId)
        playlistTrackIds.forEach {
            when (it == trackId) {
                true -> isExists = true
                false -> {}
            }
        }

        if (isExists) {
            return "Трек уже добавлен в плейлист ${playlistName}"
        } else {
            val newTrackList = playlistTrackIds + trackId
            playerDatabaseRepository.updateNumberOfTrack(newTrackList.size, playlistId)
            playerDatabaseRepository.updateTrackList(playlistId, newTrackList)
            return "Добавлено в плейлист ${playlistName}"
        }

    }


}