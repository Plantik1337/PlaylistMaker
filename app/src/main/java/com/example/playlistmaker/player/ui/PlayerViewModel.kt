package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.PlayerRepository

class PlayerViewModel(previewUrl: String, private val player: PlayerRepository) : ViewModel() {

    companion object {
        private const val DELAY = 300L
    }

    private var isSongPlaying: Boolean = false

    private val playerStatusLiveData = MutableLiveData<PlayerState>()
    private val currentTimeMutableLiveData = MutableLiveData<Int>()
    fun playerLiveData(): LiveData<PlayerState> = playerStatusLiveData
    fun currentTimeLiveData(): LiveData<Int> = currentTimeMutableLiveData

    private var mainThreadHandler = Handler(Looper.getMainLooper())

    private var currentPositionUpdateRunnable: Runnable

//    fun preparePlayer(){
//        playerStatusLiveData.postValue(PlayerState.StateDefault)
//
//        player.setDataSource(previewUrl)
//        player.prepareAsync()
//        player.setOnPreparedListener {
//            playerStatusLiveData.postValue(PlayerState.StatePrepared)
//            isSongPlaying = false
//        }
//        player.setOnCompletionListener {
//            playerStatusLiveData.postValue(PlayerState.StatePrepared)
//            isSongPlaying = false
//    }

    init {
        Log.i("dataSource",previewUrl)
        playerStatusLiveData.postValue(PlayerState.StateDefault)
        player.setDataSource(previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerStatusLiveData.postValue(PlayerState.StatePrepared)
            isSongPlaying = false
            Log.i("Player State", "Player is ready!")
            //player.start()
        }
        player.setOnCompletionListener {
            playerStatusLiveData.postValue(PlayerState.StatePrepared)
            isSongPlaying = false
        }

        currentPositionUpdateRunnable = object : Runnable {
            override fun run() {
                if (isSongPlaying) {
                    currentTimeMutableLiveData.postValue(player.getCurrentPosition())
                    mainThreadHandler.postDelayed(this, DELAY)
                    //Log.i("Time", player.getCurrentPosition().toString())
                }
            }
        }
    }

    fun playbackControl() {
        when (isSongPlaying) {
            true -> {
                stopPlayer()
                playerStatusLiveData.postValue(PlayerState.StatePaused)
            }

            false -> {
                startPlayer()
                mainThreadHandler.post(currentPositionUpdateRunnable)
                playerStatusLiveData.postValue(PlayerState.StatePlaying)
            }
        }
    }

    fun stopPlayer() {
        isSongPlaying = false
        player.pause()
    }
    fun startPlayer(){
        isSongPlaying = true
        player.start()
    }

    override fun onCleared() {
        mainThreadHandler.removeCallbacks(currentPositionUpdateRunnable)
        player.release()
        super.onCleared()
    }


}

sealed class PlayerState {
    object StateDefault : PlayerState()
    object StatePrepared : PlayerState()
    object StatePlaying : PlayerState()
    object StatePaused : PlayerState()
}

