package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.Track

interface HistoryRepository {
    fun read(): ArrayList<Track>
    fun write(track: Track)
    fun clearHistory()
    fun returnFirst(): Track
}