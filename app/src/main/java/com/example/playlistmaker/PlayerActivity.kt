package com.example.playlistmaker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val historyTransaction = HistoryTransaction()
        val sharedPreferences = getSharedPreferences(HISTORY_LIST, MODE_PRIVATE)
        val contentForPage = historyTransaction.read(sharedPreferences)

        val duration = findViewById<TextView>(R.id.playerSongDurationTextViewData)
        val album = findViewById<TextView>(R.id.playerAlbumNameData)
        val yearOfRelease = findViewById<TextView>(R.id.yearOfReliseData)
        val genre = findViewById<TextView>(R.id.genreData)
        val country = findViewById<TextView>(R.id.сountryData)

        duration.setText(contentForPage[0].trackTimeMillis)
//        if (contentForPage[0].collectionName.isNullOrEmpty() or contentForPage[0].collectionName.equals(contentForPage[0].trackName) ){
//            album.visibility = View.GONE
//        }else{}
            album.setText(contentForPage[0].collectionName)

        yearOfRelease.setText(contentForPage[0].releaseDate)
        genre.setText(contentForPage[0].primaryGenreName)
        country.setText(contentForPage[0].country)
    }
}