package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.search.Statement
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

    override fun doRequest(expression: String): Flow<Statement> = flow {
        val result = networkClient.callMusicResponse(expression)
        when (result) {
            is Response.Error -> emit(Statement.Error(result.errorMessage))
            is Response.Success -> emit(Statement.Success(result.data.results))
        }
    }

    override fun write(track: Track) {
        history.write(track)
    }

}