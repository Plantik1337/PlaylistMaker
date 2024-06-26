package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.MusicResponse

interface NetworkClient {
    fun isNetworkAvalible():Boolean
    suspend fun callMusicResponse(exception: String): Response<MusicResponse>
}
sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val errorMessage: String) : Response<Nothing>()
}