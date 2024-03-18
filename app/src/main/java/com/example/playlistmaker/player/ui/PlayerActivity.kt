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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.Track
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.PlayerRepository
import com.example.playlistmaker.player.PlayerRepositoryImpl
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val DELAY = 300L
    }


    //private var playerState = STATE_DEFAULT
    private lateinit var play: ImageView
    private var mediaPlayer = MediaPlayer()

    //private val player: PlayerRepository = PlayerRepositoryImpl(mediaPlayer)
    private var mainThreadHandler = Handler(Looper.getMainLooper())

    private lateinit var viewModel: PlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val currentContent = Track(
            intent.getStringExtra("trackName").toString(),
            intent.getStringExtra("artistName").toString(),
            intent.getStringExtra("trackTimeMillis").toString(),
            intent.getStringExtra("artworkUrl100").toString(),
            intent.getStringExtra("previewUrl").toString(),
            intent.getStringExtra("releaseDate").toString(),
            intent.getStringExtra("country").toString(),
            intent.getStringExtra("primaryGenreName").toString(),
            intent.getStringExtra("collectionName").toString(),
            intent.getStringExtra("collectionExplicitness").toString()
        )

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(mediaPlayer, currentContent.previewUrl)
        )[PlayerViewModel::class.java]

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

        viewModel.playerLiveData().observe(this) { screenState ->
            when (screenState) {
                PlayerState.StateDefault -> {
                    play.isEnabled = false
                }

                PlayerState.StatePaused -> {
                    binding.playPauseButton.setImageResource(R.drawable.baseline_play_circle_24)
                }

                PlayerState.StatePlaying -> {
                    binding.playPauseButton.setImageResource((R.drawable.baseline_pause_circle_24))
                }
                PlayerState.StatePrepared -> {
                    play.isEnabled = true
                    binding.trackTimeView.text = "0:00"
                }
            }
        }

        play.setOnClickListener {

            viewModel.playbackControl()

            object : Runnable {
                override fun run() {
                    if (mediaPlayer.isPlaying) {
                        binding.trackTimeView.text = SimpleDateFormat(
                            "m:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                        mainThreadHandler.postDelayed(this, DELAY)
                    }
                }
            }.run()
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
                binding.playerAlbumNameData.text =
                    currentContent.collectionName.substring(0, maxSymbols) + "..."
            } else {
                binding.playerAlbumNameData.text = currentContent.collectionName
            }

        }
        binding.yearOfReliseData.text = currentContent.releaseDate.substring(0, 4)
        binding.genreData.text = currentContent.primaryGenreName
        binding.countryData.text = currentContent.country

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}