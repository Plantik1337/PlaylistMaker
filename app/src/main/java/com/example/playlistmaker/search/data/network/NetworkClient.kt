package com.example.playlistmaker.search.data.network

import android.content.Context
import com.example.playlistmaker.search.data.MusicResponse

interface NetworkClient {
    fun isNetworkAvalible():Boolean
    fun callMusicResponse(exception: String): Response<MusicResponse>
}
sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val errorMessage: String) : Response<Nothing>()
}