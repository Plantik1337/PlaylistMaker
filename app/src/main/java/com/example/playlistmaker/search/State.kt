package com.example.playlistmaker.search

import com.example.playlistmaker.Track

//data class State(val statement: Statement, val trackList: List<Track>)

sealed class Statement {
    //object ERROR_INTERNET : Statement()
    //object ERROR_NOT_FOUND : Statement()
    //object SUCCESS : Statement()
    //data class Request(val trackList: List<Track>): Statement()
    data class HISTORY(val trackList: List<Track>) : Statement()
    data class Error(val errorMessage: String) : Statement()
    data class Success(val trackList: List<Track>) : Statement()
}
