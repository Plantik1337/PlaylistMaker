package com.example.playlistmaker.search.domain

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.search.Statement

interface Interactor {
    fun read(sharedPreferences: SharedPreferences): List<Track>
    fun doRequest(expression: String, context: Context): Statement
    fun clearHistory(sharedPreferences: SharedPreferences)
    fun write(sharedPreferences: SharedPreferences, track: Track)
}