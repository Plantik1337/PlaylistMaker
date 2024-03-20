package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.Track

interface HistoryRepository {
    fun read(): ArrayList<Track>
    fun write(track: Track)
    fun clearHistory()
    fun returnFirst(): Track
}