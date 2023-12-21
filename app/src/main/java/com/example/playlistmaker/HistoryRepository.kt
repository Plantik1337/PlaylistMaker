package com.example.playlistmaker

import android.content.SharedPreferences

interface HistoryRepository {
    fun read(sharedPreferences: SharedPreferences): ArrayList<Track>
    fun write(sharedPreferences: SharedPreferences, track: Track)
    fun clearHistory(sharedPreferences: SharedPreferences)
    fun returnFirst(sharedPreferences: SharedPreferences): Track
}