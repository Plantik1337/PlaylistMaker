package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.search.data.MusicResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    override suspend fun callMusicResponse(exception: String): Response<MusicResponse> {
        if(isNetworkAvalible() == false){
            return Response.Error("-1")
        }
        return withContext(Dispatchers.IO){
            try {
                val response = appleServiceapit.search(exception)
                Response.Success(response)
            }catch (e: Throwable){
                Response.Error("500")
            }
        }
    }

}