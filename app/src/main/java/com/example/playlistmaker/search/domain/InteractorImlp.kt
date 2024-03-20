package com.example.playlistmaker.search.domain

import android.content.Context
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.search.Statement
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.Response

class InteractorImlp(
    private val history: HistoryRepository,
    private val networkClient: NetworkClient
) : Interactor {

    override fun read(): List<Track> {
        return history.read()
    }

    override fun clearHistory() {
        history.clearHistory()
    }

    override fun doRequest(expression: String): Statement {
        val result =
            networkClient.callMusicResponse(expression)
        return when (result) {
            is Response.Error -> Statement.Error(result.errorMessage)
            is Response.Success -> Statement.Success(result.data.results)
        }
    }

    override fun write(track: Track) {
        history.write(track)
    }

}