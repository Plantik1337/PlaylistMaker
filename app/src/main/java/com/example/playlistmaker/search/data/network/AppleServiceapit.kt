package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.MusicResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleServiceapit{
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<MusicResponse>
}