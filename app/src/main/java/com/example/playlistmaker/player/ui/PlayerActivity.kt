package com.example.playlistmaker.player.ui

import android.graphics.Outline
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.PlayerRepository
import com.example.playlistmaker.player.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.HISTORY_LIST
import com.example.playlistmaker.search.data.HistoryTransaction
import com.example.playlistmaker.search.domain.HistoryRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 300L
    }


    private var playerState = STATE_DEFAULT
    private lateinit var play: ImageView
    private var mediaPlayer = MediaPlayer()
    private val player: PlayerRepository = PlayerRepositoryImpl(mediaPlayer)
    private val history: HistoryRepository = HistoryTransaction()
    private var mainThreadHandler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentContent =
            history.returnFirst(getSharedPreferences(HISTORY_LIST, MODE_PRIVATE))

        val backButton = findViewById<ImageButton>(R.id.menu_button)
        backButton.setOnClickListener {
            finish()
        }

        Log.i("Data", currentContent.toString())
        play = findViewById(R.id.playPauseButton)

        binding.songNamePlayer.text = currentContent.trackName
        binding.autorName.text = currentContent.artistName

        Glide.with(binding.albumPlayerImageView)
            .load(currentContent.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(binding.albumPlayerImageView)
        binding.albumPlayerImageView.clipToOutline = true
        binding.albumPlayerImageView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val cornerRadius = 15
                outline.setRoundRect(0, 0, view.width, view.height, cornerRadius.toFloat())
            }
        }
        //// Prepare player
        player.setDataSource(currentContent.previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        player.setOnCompletionListener {
            binding.trackTimeView.text = "0:00"
            playerState = STATE_PREPARED
        }

        val runnable = object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    binding.trackTimeView.text = SimpleDateFormat(
                        "m:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    mainThreadHandler.postDelayed(this, DELAY)
                }
            }
        }

        play.setOnClickListener {
            playbackControl()
            runnable.run()
        }

        binding.playerSongDurationTextViewData.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(currentContent.trackTimeMillis.toLong())

        if (currentContent.collectionName.contains("single")) {
            binding.playerAlbumName.visibility = View.GONE
            binding.playerAlbumNameData.visibility = View.GONE
        } else {
            val maxSymbols = 30

            if (currentContent.collectionName.length > maxSymbols) {
                binding.playerAlbumNameData.text = currentContent.collectionName.substring(0, maxSymbols) + "..."
            } else {
                binding.playerAlbumNameData.text = currentContent.collectionName
            }

        }
        binding.yearOfReliseData.text = currentContent.releaseDate.substring(0, 4)
        binding.genreData.text = currentContent.primaryGenreName
        binding.countryData.text = currentContent.country

    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        player.start()
        play.setImageResource(R.drawable.baseline_pause_circle_24)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        player.pause()
        play.setImageResource(R.drawable.baseline_play_circle_24)
        playerState = STATE_PAUSED
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}