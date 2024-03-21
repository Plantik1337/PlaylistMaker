package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.search.Statement

interface Interactor {
    fun read(): List<Track>
    fun doRequest(expression: String): Statement
    fun clearHistory()
    fun write(track: Track)
}