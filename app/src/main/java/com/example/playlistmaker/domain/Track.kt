package com.example.playlistmaker.domain

data class MusicResponse(
    val resultCount: String,
    val results: ArrayList<Track>
)

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val previewUrl: String,
    val releaseDate: String,
    val country: String,
    val primaryGenreName: String,
    val collectionName: String,
    val collectionExplicitness: String,
)