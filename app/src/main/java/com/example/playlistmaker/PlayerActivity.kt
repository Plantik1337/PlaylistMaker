package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale

class PlayerActivity : AppCompatActivity() {


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

        val trackName = findViewById<TextView>(R.id.songNamePlayer)
        val autorName = findViewById<TextView>(R.id.autorName)
        val albumImage = findViewById<ImageView>(R.id.albumPlayerImageView)
        val duration = findViewById<TextView>(R.id.playerSongDurationTextViewData)
        val album = findViewById<TextView>(R.id.playerAlbumNameData)
        val albumMessage = findViewById<TextView>(R.id.playerAlbumName)
        val yearOfRelease = findViewById<TextView>(R.id.yearOfReliseData)
        val genre = findViewById<TextView>(R.id.genreData)
        val country = findViewById<TextView>(R.id.сountryData)

        trackName.setText(contentForPage[0].trackName)
        autorName.setText(contentForPage[0].artistName)

        Glide.with(albumImage)
            .load(contentForPage[0].artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(albumImage)

        duration.setText(
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(contentForPage[0].trackTimeMillis.toLong())
        )

        if(contentForPage[0].collectionExplicitness == "notExplicit"){
            albumMessage.visibility = View.GONE
            album.visibility = View.GONE
        }
        else{
            album.setText(contentForPage[0].collectionName)
        }
        yearOfRelease.setText(contentForPage[0].releaseDate.substring(0, 4))
        genre.setText(contentForPage[0].primaryGenreName)
        country.setText(contentForPage[0].country)
    }
}