package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.MusicResponse

interface NetworkClient {
    fun isNetworkAvalible():Boolean
    suspend fun callMusicResponse(exception: String): Response
}
sealed class Response {
    data class Success(val data: MusicResponse) : Response()
    data class Error(val errorMessage: String) : Response()
}