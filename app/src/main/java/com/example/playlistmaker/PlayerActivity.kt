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
        private const val DELAY = 400L
    }

    private var mainThreadHandler = Handler(Looper.getMainLooper())

    private var playerState = STATE_DEFAULT
    private lateinit var play: ImageView
    private var mediaPlayer = MediaPlayer()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

         val historyTransaction = HistoryTransaction()
         val sharedPreferences = getSharedPreferences(HISTORY_LIST, MODE_PRIVATE)
         val contentForPage = historyTransaction.read(sharedPreferences)

        val backButton = findViewById<ImageButton>(R.id.menu_button)
        backButton.setOnClickListener {
            finish()
        }

        Log.i("Data",contentForPage[0].toString())
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

        trackName.text = contentForPage[0].trackName
        autorName.text = contentForPage[0].artistName

        Glide.with(albumImage)
            .load(contentForPage[0].artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
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
        mediaPlayer.setDataSource(contentForPage[0].previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }

        play.setOnClickListener {
            playbackControl()
        }

        duration.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(contentForPage[0].trackTimeMillis.toLong())

        if (contentForPage[0].collectionName.contains("single")) {
            albumMessage.visibility = View.GONE
            album.visibility = View.GONE
        } else {
            val maxSymbols = 30

            if (contentForPage[0].collectionName.length > maxSymbols) {
                album.text = contentForPage[0].collectionName.substring(0, maxSymbols) + "..."
            } else {
                album.text = contentForPage[0].collectionName
            }

        }
        yearOfRelease.text = contentForPage[0].releaseDate.substring(0, 4)
        genre.text = contentForPage[0].primaryGenreName
        country.text = contentForPage[0].country

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
        mediaPlayer.release()
    }
}