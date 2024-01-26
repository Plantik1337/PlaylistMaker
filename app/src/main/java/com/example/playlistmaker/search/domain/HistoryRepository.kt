package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.Track

interface HistoryRepository {
    fun read(sharedPreferences: SharedPreferences): ArrayList<Track>
    fun write(sharedPreferences: SharedPreferences, track: Track)
    fun clearHistory(sharedPreferences: SharedPreferences)
    fun returnFirst(sharedPreferences: SharedPreferences): Track
}