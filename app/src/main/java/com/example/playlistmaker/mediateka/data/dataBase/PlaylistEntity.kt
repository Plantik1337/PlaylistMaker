package com.example.playlistmaker.mediateka.data.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val key: Int,
    val playlistName: String,
    val description: String,
    val imageURI: String,
    val trackIdList: String,
    val numberOfTracks: Int
)