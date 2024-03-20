package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.search.data.MusicResponse

class NetworkClientImpl(
    private val appleServiceapit: AppleServiceapit,
    private val context: Context
) : NetworkClient {

    companion object {
        const val TAG = "ERROR_STATE"
    }

    override fun isNetworkAvalible(): Boolean {
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

    override fun callMusicResponse(exception: String): Response<MusicResponse> {
        if (isNetworkAvalible()) {
            val response = appleServiceapit.search(exception).execute()

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


}