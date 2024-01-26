package com.example.playlistmaker.search.data

import android.util.Log
import com.example.playlistmaker.MusicResponse
import com.example.playlistmaker.Track
import com.example.playlistmaker.search.ui.SearchActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DbConnection {
    var musicResponse = emptyList<Track>()

    fun callMusicResponse():List<Track> = musicResponse
    fun connection(expression: String): List<Track> {
        val baseUrl = "https://itunes.apple.com"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val appleMusicServer = retrofit.create(AppleMusicServer::class.java)

        appleMusicServer.search(expression)
            .enqueue(object : Callback<MusicResponse> {
                override fun onResponse(
                    call: Call<MusicResponse>,
                    response: Response<MusicResponse>
                ) {
                    if (response.body()?.results != null) {
                        Log.d(SearchActivity.TAG, "onResponse: ${response.body()?.results}")

                        when (response.code()) {
                            200 -> { // песни найдены
                                Log.i(SearchActivity.TAG, response.code().toString())
                                musicResponse = response.body()!!.results
                            }
                        }
                    } else {//песни не найдены
                        Log.e(
                            SearchActivity.TAG,
                            "Что-то пошло не так, серер не отдаёт список песен"
                        )
                    }

                }

                override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                    //Log.d(SearchActivity.TAG, "onFailure: $t")
                }
            })
        return musicResponse
    }
}