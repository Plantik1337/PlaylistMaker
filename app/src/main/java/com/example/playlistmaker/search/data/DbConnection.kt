package com.example.playlistmaker.search.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DbConnection {

    companion object {
        const val TAG = "ERROR_STATE"
    }

    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val appleMusicServer = retrofit.create(AppleMusicServer::class.java)

    private fun isNetworkAvalible(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    fun callMusicResponse(exception: String, context: Context): Response<MusicResponse> {
        if (isNetworkAvalible(context)) {
            val response = appleMusicServer.search(exception).execute()

            return if (response.isSuccessful) {
                val musicResponse: MusicResponse? = response.body()
                if (musicResponse != null) {
                    if (musicResponse.results.isNotEmpty()) {
                        //Log.i(TAG, "${response.code()} ne pustoy")
                        Response.Success(musicResponse)
                    } else {
                        Log.i(TAG, "${response.code()} pustoy")
                        Response.Error("empty")
                    }
                } else {
                    Log.e(TAG, "${response.code()}")
                    Response.Error("Failed to get music response")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Log.e(TAG, "${response.code()}")
                Response.Error(errorMessage)
            }
        } else {
            return Response.Error("-1")
        }
    }


    sealed class Response<out T> {
        data class Success<out T>(val data: T) : Response<T>()
        data class Error(val errorMessage: String) : Response<Nothing>()
    }
}