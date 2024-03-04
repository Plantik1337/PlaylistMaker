package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.Track
import com.example.playlistmaker.search.Statement

interface Interactor {
    fun read(sharedPreferences: SharedPreferences): List<Track>
    fun doRequest(expression: String): Statement
    fun clearHistory(sharedPreferences: SharedPreferences)
    fun write(sharedPreferences: SharedPreferences, track: Track)
}