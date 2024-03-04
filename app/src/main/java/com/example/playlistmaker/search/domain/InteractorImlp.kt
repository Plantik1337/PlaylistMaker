package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.Track
import com.example.playlistmaker.search.Statement
import com.example.playlistmaker.search.data.DbConnection
import com.example.playlistmaker.search.data.DbConnection.Response
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

    override fun doRequest(expression: String): Statement {
        val result =
            dbConnection.callMusicResponse(expression)
        return when (result) {
            is Response.Error -> Statement.Error(result.errorMessage)
            is Response.Success ->  Statement.Success(result.data.results)
        }
    }
    //return dbConnection.callMusicResponse()//нашлись песни

    override fun write(sharedPreferences: SharedPreferences, track: Track) {
        history.write(sharedPreferences, track)
    }

}