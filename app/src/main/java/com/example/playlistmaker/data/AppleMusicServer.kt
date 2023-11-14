package com.example.playlistmaker.data

import com.example.playlistmaker.domain.MusicResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleMusicServer {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<MusicResponse>
}