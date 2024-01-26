package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.Track
import com.example.playlistmaker.search.data.DbConnection
import com.example.playlistmaker.search.data.HistoryTransaction

class InteractorImlp : Interactor {
    private val history: HistoryRepository = HistoryTransaction()
    private val dbConnection = DbConnection()

    override fun read(sharedPreferences: SharedPreferences): List<Track> {
        return history.read(sharedPreferences)
    }

    override fun clearHistory(sharedPreferences: SharedPreferences) {
        history.clearHistory(sharedPreferences)
    }

    override fun doRequest(expression: String): List<Track> {
        val request = dbConnection.connection(expression)
        return if (request.size > 0) {
            //Log.i("OnRespoce", "Нашлись песни")
            dbConnection.callMusicResponse()//нашлись песни
        } else {
            //Log.e("OnFailure", "Песни не нашлись")
            emptyList<Track>()//ничего не нашлось, вернуть пустой список
        }
    }

    override fun write(sharedPreferences: SharedPreferences, track: Track) {
        history.write(sharedPreferences, track)
    }

}