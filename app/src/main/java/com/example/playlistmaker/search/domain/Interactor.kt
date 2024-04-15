package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.search.Statement
import kotlinx.coroutines.flow.Flow

interface Interactor {
    fun read(): List<Track>
    fun doRequest(expression: String): Flow<Statement>
    fun clearHistory()
    fun write(track: Track)
}