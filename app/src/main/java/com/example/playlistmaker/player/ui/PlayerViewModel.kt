package com.example.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.PlayerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(previewUrl: String, private val player: PlayerRepository) : ViewModel() {

    companion object {
        private const val DELAY = 300L
    }

    //private var isSongPlaying: Boolean = false

    private var timerJob: Job? = null

    private val playerStatusLiveData = MutableLiveData<PlayerState>(PlayerState.StateDefault())

    fun playerLiveData(): LiveData<PlayerState> = playerStatusLiveData
    //fun currentTimeLiveData(): LiveData<Int> = currentTimeMutableLiveData

    //private var mainThreadHandler = Handler(Looper.getMainLooper())

    //private var currentPositionUpdateRunnable: Runnable

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

        initMediaPlayer(previewUrl)

//        Log.i("dataSource", previewUrl)
//        playerStatusLiveData.postValue(PlayerState.StateDefault)
//        player.setDataSource(previewUrl)
//        player.prepareAsync()
//        player.setOnPreparedListener {
//            playerStatusLiveData.postValue(PlayerState.StatePrepared)
//            isSongPlaying = false
//            Log.i("Player State", "Player is ready!")
//            //player.start()
//        }
//        player.setOnCompletionListener {
//            playerStatusLiveData.postValue(PlayerState.StatePrepared)
//            isSongPlaying = false
//        }

//        currentPositionUpdateRunnable = object : Runnable {
//            override fun run() {
//                if (isSongPlaying) {
//                    currentTimeMutableLiveData.postValue(player.getCurrentPosition())
//                    mainThreadHandler.postDelayed(this, DELAY)
//                    //Log.i("Time", player.getCurrentPosition().toString())
//                }
//            }
//        }
    }

    private fun initMediaPlayer(previewUrl: String) {
        //Log.i("dataSource", previewUrl)
        //playerStatusLiveData.postValue(PlayerState.StateDefault())
        player.setDataSource(previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerStatusLiveData.postValue(PlayerState.StatePrepared())
            Log.i("Player State", "Player is ready!")
        }
        player.setOnCompletionListener {
            playerStatusLiveData.postValue(PlayerState.StatePrepared())
        }
    }

    fun playbackControl() {
        when (playerStatusLiveData.value) {
            is PlayerState.StatePlaying -> {
                stopPlayer()
            }

            is PlayerState.StatePrepared, is PlayerState.StatePaused -> {
                startPlayer()
            }

            else -> {}
        }
    }

    fun stopPlayer() {
        timerJob?.cancel()
        player.pause()
        playerStatusLiveData.postValue(PlayerState.StatePaused(player.getCurrentPosition()))
    }

    fun startPlayer() {
        player.start()
        playerStatusLiveData.postValue(PlayerState.StatePlaying(player.getCurrentPosition()))
        startTimer()
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (player.isPlayeing()) {
                delay(DELAY)
                playerStatusLiveData.postValue(PlayerState.StatePlaying(player.getCurrentPosition()))
            }
        }
    }


}

sealed class PlayerState(
    val isPlayButtonEnabled: Boolean,
    val buttonText: String,
    val progress: String
) {
    class StateDefault : PlayerState(false, "PLAY", "00:00")
    class StatePrepared : PlayerState(true, "PLAY", "00:00")
    class StatePlaying(progress: String) : PlayerState(true, "PAUSE", progress)
    class StatePaused(progress: String) : PlayerState(true, "PLAY", progress)
}

