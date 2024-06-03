package com.example.playlistmaker.mediateka.data

import android.util.Log
import com.example.playlistmaker.mediateka.data.convertors.PlaylistConvertor
import com.example.playlistmaker.mediateka.data.dataBase.AppDatabase
import com.example.playlistmaker.mediateka.data.dataBase.PlaylistEntity
import com.example.playlistmaker.mediateka.domain.create_playlist.CreatePlaylistRepository

class CreatePlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistConvertor
) : CreatePlaylistRepository {
    override suspend fun addNewPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().newPlaylist(convertToentity(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val myPlaylist = convertToentity(playlist)
        Log.i("мой плейлист", myPlaylist.toString())
        appDatabase.playlistDao().updatePlaylistInfo(myPlaylist)
    }

    private fun convertToentity(playlist: Playlist): PlaylistEntity {
        return playlist.let { convertor.map(it) }
    }
}