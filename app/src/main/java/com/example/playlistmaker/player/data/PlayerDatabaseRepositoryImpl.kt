package com.example.playlistmaker.player.data

import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.mediateka.data.PlaylistEntity
import com.example.playlistmaker.mediateka.data.convertors.PlaylistConvertor
import com.example.playlistmaker.mediateka.data.dataBase.AppDatabase
import com.example.playlistmaker.mediateka.data.dataBase.TrackEntity
import com.example.playlistmaker.mediateka.data.convertors.TrackDbConvertor
import com.example.playlistmaker.search.data.Track

class PlayerDatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
    private val playlistConvertor: PlaylistConvertor
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

    override suspend fun getPlaylists(): List<Playlist> {
        return convertFromPlatlistEntity(appDatabase.playlistDao().getPlaylists())
    }

    override suspend fun isTrackExistInPlaylist(playlistId: Int): List<Track> {
        return playlistConvertor.fromEntityTrackIdList(
            appDatabase.playlistDao().isExistsInPlaylist(playlistId)
        )
    }

    override suspend fun updateTrackList(playlistId: Int, newTrackList: List<Track>) {
        appDatabase.playlistDao()
            .updateTrackList(playlistConvertor.toEntityTrackIdList(newTrackList), playlistId)
    }

    override suspend fun updateNumberOfTrack(newNumber: Int, playlistId: Int) {
        appDatabase.playlistDao().updateNumberOfTracks(newNumber, playlistId)
    }

    private fun convertFromPlatlistEntity(playlistEntity: List<PlaylistEntity>): List<Playlist> {
        return playlistEntity.map { playlistEntities -> playlistConvertor.map(playlistEntities) }
    }

    private fun convertToEntity(track: Track): TrackEntity {
        return track.let { trackDbConvertor.map(it) }
    }
}