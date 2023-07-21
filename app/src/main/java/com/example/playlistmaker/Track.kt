package com.example.playlistmaker

//data class Track(
//   val trackName: String, // Название композиции
//   val artistName: String, // Имя исполнителя
//   val trackTime: String, // Продолжительность трека
//   val artworkUrl100: String // Ссылка на изображение обложки
//)

//data class MusicResponse(
//    val resultCount: String,
//    val results: Track
//)

data class MusicResponse(
    val resultCount: String,
    val results: ArrayList<Track>
)

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
)