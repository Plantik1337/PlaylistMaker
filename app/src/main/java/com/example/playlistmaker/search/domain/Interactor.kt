package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.Track

interface Interactor {
    fun read(sharedPreferences: SharedPreferences): List<Track>
    fun doRequest(expression: String): List<Track>
    fun clearHistory(sharedPreferences: SharedPreferences)
    fun write(sharedPreferences: SharedPreferences, track: Track)
}