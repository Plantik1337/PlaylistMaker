package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.player.PlayerRepository
import com.example.playlistmaker.player.PlayerRepositoryImpl
import com.example.playlistmaker.player.ui.PlayerState.StatePlaying

@Suppress("UNCHECKED_CAST")
class PlayerViewModel(val mediaPlayer: MediaPlayer, val previewUrl: String) : ViewModel() {

    private var playerStatusLiveData = MutableLiveData<PlayerState>()
    fun playerLiveData(): LiveData<PlayerState> = playerStatusLiveData

    private var isSongPlaying: Boolean = false


    companion object {
        fun getViewModelFactory(
            mediaPlayer: MediaPlayer,
            previewUrl: String
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(mediaPlayer, previewUrl) as T
                }
            }
    }


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

