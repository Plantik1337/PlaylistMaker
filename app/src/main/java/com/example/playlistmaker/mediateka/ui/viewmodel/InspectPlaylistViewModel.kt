package com.example.playlistmaker.mediateka.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.data.InspectPlaylistRepository
import com.example.playlistmaker.search.data.Track
import kotlinx.coroutines.launch

class InspectPlaylistViewModel(private val repository: InspectPlaylistRepository) : ViewModel() {

    var trackListMl = MutableLiveData<List<Track>>()

    fun trackLsitLiveData(): LiveData<List<Track>> = trackListMl
    fun getTrackList(trackIdList: List<String>) {
        viewModelScope.launch {
            trackListMl.postValue(repository.getTrackList(trackIdList))
        }

    }

    fun deleteTrack(id: Int, playlistKey: Int) {
        viewModelScope.launch {
            Log.i("Статус удаления","Пытаюсь удалить")
            repository.deleteTrack(id, playlistKey)
        }

    }
}