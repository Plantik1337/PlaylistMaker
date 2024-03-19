package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.PlayerRepository
import com.example.playlistmaker.player.PlayerRepositoryImpl

class PlayerViewModel(private val mediaPlayer: MediaPlayer, private val previewUrl: String) : ViewModel() {

    private val playerStatusLiveData = MutableLiveData<PlayerState>()
    fun playerLiveData(): LiveData<PlayerState> = playerStatusLiveData

    private var isSongPlaying: Boolean = false

    private val player: PlayerRepository = PlayerRepositoryImpl(mediaPlayer)


    init {
        playerStatusLiveData.postValue(PlayerState.StateDefault)

        player.setDataSource(previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerStatusLiveData.postValue(PlayerState.StatePrepared)
        }
        player.setOnCompletionListener {
            playerStatusLiveData.postValue(PlayerState.StatePrepared)
        }
    }

    fun playbackControl() {
        when (isSongPlaying) {
            true -> {
                isSongPlaying = false
                player.pause()
                playerStatusLiveData.postValue(PlayerState.StatePaused)
            }

            false -> {
                isSongPlaying = true
                player.start()
                playerStatusLiveData.postValue(PlayerState.StatePlaying)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }


}

sealed class PlayerState {
    object StateDefault : PlayerState()
    object StatePrepared : PlayerState()
    object StatePlaying : PlayerState()
    object StatePaused : PlayerState()
}

