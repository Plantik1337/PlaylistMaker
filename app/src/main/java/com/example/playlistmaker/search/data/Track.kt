package com.example.playlistmaker.search.data

data class MusicResponse(
    val resultCount: String,
    val results: List<Track>
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