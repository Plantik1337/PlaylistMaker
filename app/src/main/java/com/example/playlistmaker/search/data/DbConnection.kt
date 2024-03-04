package com.example.playlistmaker.search.data

import com.example.playlistmaker.MusicResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DbConnection {

    companion object {
        const val TAG = "MUSIC_STATE"
    }

    //private val serverLiveData = MutableLiveData<ResponseFromServer>()
    //fun getServerResponse(): LiveData<ResponseFromServer> = serverLiveData

    //var musicResponse = emptyList<Track>()

    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val appleMusicServer = retrofit.create(AppleMusicServer::class.java)

//    sealed class ResponseFromServer {
//        object Failure : ResponseFromServer()
//
//        //object BadResponse : ResponseFromServer()
//        data class Successful(val serverResponse: List<Track>) : ResponseFromServer()
//    }

    fun callMusicResponse(exception: String): Response<MusicResponse> {
        val response = appleMusicServer.search(exception).execute()

        return if (response.isSuccessful) {
            val musicResponse: MusicResponse? = response.body()
            if (musicResponse != null) {
                Response.Success(musicResponse)
            } else {
                Response.Error("Failed to get music response")
            }
        } else {
            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
            //response.code()
            Response.Error(errorMessage)
        }
    }

    sealed class Response<out T> {
        data class Success<out T>(val data: T) : Response<T>()
        data class Error(val errorMessage: String) : Response<Nothing>()
    }

//    fun callMusicResponse(expression: String): ResponseFromServer {
//                connection(expression)
//        return when (musicResponse) {
//            emptyList<Track>() -> {
//                ResponseFromServer.Failure
//            }
//
//            else -> {
//                ResponseFromServer.Successful(musicResponse)
//            }
//        }
//    }


//    private fun callMusicResponse(expression: String) {
//        appleMusicServer.search(expression).enqueue(object : Callback<MusicResponse> {
//
//            override fun onResponse(
//                call: Call<MusicResponse>,
//                response: Response<MusicResponse>
//            ) {
//                Log.d(TAG, "onResponse: $expression, ${response.body()?.results}")
//                if (response.isSuccessful) {
//                    serverLiveData.postValue(ResponseFromServer.Successful(response.body()!!.results))
//                    //response.body()!!.results
//                    //Log.i(TAG, response.body()!!.results.toString())
//
//                } else {//песни не найдены
//                    Log.e(
//                        TAG,
//                        "Что-то пошло не так, серер не отдаёт список песен"
//                    )
//                    //response.body()!!.results
//                }
//                //Log.i("Что в MR находится после запроса",musicResponse.toString())
//
//            }
//
//            override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
//                Log.d(TAG, "onFailure: $t")
//                serverLiveData.postValue(ResponseFromServer.Failure)
//            }
//
//        })
//        //Log.i("Что в MR находится",musicResponse.toString())
//    }
}