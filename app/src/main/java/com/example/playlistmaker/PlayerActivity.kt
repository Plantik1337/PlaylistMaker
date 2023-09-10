package com.example.playlistmaker

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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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

    private var mainThreadHandler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val historyTransaction = HistoryTransaction()
        val sharedPreferences = getSharedPreferences(HISTORY_LIST, MODE_PRIVATE)
        val contentForPage = historyTransaction.read(sharedPreferences)
        val currentContent = contentForPage[0]

        val backButton = findViewById<ImageButton>(R.id.menu_button)
        backButton.setOnClickListener {
            finish()
        }

        Log.i("Data", currentContent.toString())
        play = findViewById(R.id.playPauseButton)
        val trackName = findViewById<TextView>(R.id.songNamePlayer)
        val autorName = findViewById<TextView>(R.id.autorName)
        val albumImage = findViewById<ImageView>(R.id.albumPlayerImageView)
        val duration = findViewById<TextView>(R.id.playerSongDurationTextViewData)
        val album = findViewById<TextView>(R.id.playerAlbumNameData)
        val albumMessage = findViewById<TextView>(R.id.playerAlbumName)
        val yearOfRelease = findViewById<TextView>(R.id.yearOfReliseData)
        val genre = findViewById<TextView>(R.id.genreData)
        val country = findViewById<TextView>(R.id.сountryData)
        val trakTime = findViewById<TextView>(R.id.trackTimeView)

        trackName.text = currentContent.trackName
        autorName.text = currentContent.artistName

        Glide.with(albumImage)
            .load(currentContent.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(albumImage)
        albumImage.clipToOutline = true
        albumImage.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val cornerRadius = 15
                outline.setRoundRect(0, 0, view.width, view.height, cornerRadius.toFloat())
            }
        }
        //// Prepare player
        mediaPlayer.setDataSource(currentContent.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            trakTime.text = "0:00"
            playerState = STATE_PREPARED
        }

        val runnable = object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    trakTime.text = SimpleDateFormat(
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

        duration.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(currentContent.trackTimeMillis.toLong())

        if (currentContent.collectionName.contains("single")) {
            albumMessage.visibility = View.GONE
            album.visibility = View.GONE
        } else {
            val maxSymbols = 30

            if (currentContent.collectionName.length > maxSymbols) {
                album.text = currentContent.collectionName.substring(0, maxSymbols) + "..."
            } else {
                album.text = currentContent.collectionName
            }

        }
        yearOfRelease.text = currentContent.releaseDate.substring(0, 4)
        genre.text = currentContent.primaryGenreName
        country.text = currentContent.country

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
        mediaPlayer.start()
        play.setImageResource(R.drawable.baseline_pause_circle_24)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.setImageResource(R.drawable.baseline_play_circle_24)
        playerState = STATE_PAUSED
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        //mainThreadHandler.removeCallbacks()
        mediaPlayer.release()
    }
}