package com.example.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(trackId: Int, previewUrl: String, private val player: PlayerRepository) :
    ViewModel() {

    companion object {
        private const val DELAY = 300L
    }

    //private val myTrack = track

    private var timerJob: Job? = null

    private val playerStatusLiveData = MutableLiveData<PlayerState>(PlayerState.StateDefault())

    fun playerLiveData(): LiveData<PlayerState> = playerStatusLiveData

    private val isTrackLikedML = MutableLiveData<Boolean>()
    fun isTrackLiked(): LiveData<Boolean> = isTrackLikedML

    init {
        initMediaPlayer(previewUrl)
        viewModelScope.launch {
            isExists(trackId)
        }
        //isTrackLikedML.postValue(player.isLiked(track.trackId))
        //trackNum = trackId
    }


    private fun initMediaPlayer(previewUrl: String) {
        player.setDataSource(previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerStatusLiveData.postValue(PlayerState.StatePrepared())
            Log.i("Player State", "Player is ready!")
        }
        player.setOnCompletionListener {
            timerJob?.cancel()
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
        timerJob?.cancel()
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

    suspend fun isExists(trackInt: Int): Boolean {
        isTrackLikedML.postValue(player.isExists(trackId = trackInt))
        // Log.i("Трек существует ?", "${isTrackLikedML.value}")

        return player.isExists(trackId = trackInt)
    }

    fun likeClickInteractor(track: Track) {

        viewModelScope.launch {
            when (isExists(track.trackId)) {
                true -> {
                    player.deleteTrack(track.trackId)
                    isTrackLikedML.postValue(false)
                    Log.i("Нажатие", "удаляем трек")
                }

                false -> {
                    player.likeTrack(track)
                    isTrackLikedML.postValue(true)
                    Log.i("Нажатие", "добавляем трек")
                }
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

