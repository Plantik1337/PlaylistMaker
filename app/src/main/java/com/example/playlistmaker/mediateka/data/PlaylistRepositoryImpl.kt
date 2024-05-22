package com.example.playlistmaker.mediateka.data

import com.example.playlistmaker.mediateka.data.convertors.PlaylistConvertor
import com.example.playlistmaker.mediateka.data.dataBase.AppDatabase

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistConvertor
) : PlaylistRepository {
    override suspend fun getPlaylists(): List<Playlist> {
        val playlistEntity =
            appDatabase.playlistDao().getPlaylists()
        return convertFromEntity(playlistEntity)
    }

    private fun convertFromEntity(playlistEntity: List<PlaylistEntity>): List<Playlist> {
        return playlistEntity.map { playlistEntities -> convertor.map(playlistEntities) }
    }
}