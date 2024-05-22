package com.example.playlistmaker.mediateka.data

import androidx.room.PrimaryKey

data class Playlist(
    val playlistName: String,
    val description: String?,
    val imageURI: String?,
    val trackIdList: List<String>,
    val numberOfTracks: Int

)
