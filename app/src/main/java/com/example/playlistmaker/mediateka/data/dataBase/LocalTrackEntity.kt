package com.example.playlistmaker.mediateka.data.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_tracks_table")
data class LocalTrackEntity(
    @PrimaryKey
    val id: Int,
    val addTime: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val previewUrl: String,
    val releaseDate: String,
    val country: String,
    val primaryGenreName: String,
    val collectionName: String,
    val collectionExplicitness: String
)
