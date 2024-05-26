package com.example.playlistmaker.mediateka.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val key: Int = 0,
    val playlistName: String,
    val description: String,
    val imageURI: String,
    val trackIdList: String,
    val numberOfTracks: Int
)