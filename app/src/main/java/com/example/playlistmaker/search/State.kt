package com.example.playlistmaker.search

import com.example.playlistmaker.search.data.Track

sealed class Statement {
    data class HISTORY(val trackList: List<Track>) : Statement()
    data class Error(val errorMessage: String) : Statement()
    data class Success(val trackList: List<Track>) : Statement()
    object Loading: Statement()
}
