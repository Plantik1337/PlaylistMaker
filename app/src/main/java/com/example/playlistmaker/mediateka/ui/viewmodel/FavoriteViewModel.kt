package com.example.playlistmaker.mediateka.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.mediateka.domain.favorite.FavoriteInteractor
import com.example.playlistmaker.search.data.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val interactor: FavoriteInteractor) : ViewModel() {
    init {}// блок init не выводится

    var trackListML = MutableLiveData<List<Track>>()

    fun tracksLiveData(): LiveData<List<Track>> = trackListML
    fun showLog() {
        Log.i(
            "FavoriteFragment - ViewModel",
            "FavoriteFragment - ViewModel была создана! Доказательста: 123"
        )
    }

    fun getTracks() {
        CoroutineScope(Dispatchers.Main).launch {
            interactor.favoriteTracks().collect{trackList ->
                Log.i("LikedTrack", trackList.toString())
                trackListML.postValue(trackList)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}