package com.example.playlistmaker.player.data

import com.example.playlistmaker.mediateka.data.dataBase.AppDatabase
import com.example.playlistmaker.mediateka.data.dataBase.TrackEntity
import com.example.playlistmaker.mediateka.data.convertors.TrackDbConvertor
import com.example.playlistmaker.search.data.Track

class PlayerDatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : PlayerDatabaseRepository {
    override suspend fun likeTrack(track: Track) {
        appDatabase.trackDao().insertTrack(convertToEntity(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        appDatabase.trackDao().deleteById(trackId)
    }

    override suspend fun isExists(trackId: Int): Boolean {
        return appDatabase.trackDao().isExists(trackId)
    }

    private fun convertToEntity(track: Track): TrackEntity {
        return track.let { track -> trackDbConvertor.map(track) }
    }
}