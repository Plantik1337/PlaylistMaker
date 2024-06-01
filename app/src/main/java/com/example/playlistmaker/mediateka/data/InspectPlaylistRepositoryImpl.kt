package com.example.playlistmaker.mediateka.data

import android.util.Log
import com.example.playlistmaker.mediateka.data.convertors.PlaylistConvertor
import com.example.playlistmaker.mediateka.data.convertors.TrackDbConvertor
import com.example.playlistmaker.mediateka.data.dataBase.AppDatabase
import com.example.playlistmaker.mediateka.data.dataBase.LocalTrackEntity
import com.example.playlistmaker.mediateka.data.dataBase.PlaylistEntity
import com.example.playlistmaker.search.data.Track
import java.util.regex.Pattern

class InspectPlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: TrackDbConvertor,
    private val playlistConvertor: PlaylistConvertor
) : InspectPlaylistRepository {
    override suspend fun deletePlaylist(key: Int) {
        appDatabase.playlistDao().deletPlaylistByKey(key)
    }

    override suspend fun deleteTrack(id: Int, playlistKey: Int) {
        val counter = appDatabase.playlistDao().getAllTrackIdLists()

        if (trackCounter(counter.toString(), id) == 1) {
            val currentPlalist =
                convertFromPlaylistEntity(appDatabase.playlistDao().getPlaylistByKey(playlistKey))

            appDatabase.playlistDao().updateTrackList(
                playlistConvertor.toEntityTrackIdList(
                    removeTrackId(
                        currentPlalist.trackIdList,
                        id
                    )
                ), playlistKey
            )
            appDatabase.playlistDao()
                .updateNumberOfTracks(currentPlalist.numberOfTracks - 1, playlistKey)

            appDatabase.localTrackDao().deleteById(id)

            Log.i("Трек удалён", "Удаалён трек с id - ${id}")
        } else {
            val currentPlalist =
                convertFromPlaylistEntity(appDatabase.playlistDao().getPlaylistByKey(playlistKey))

            appDatabase.playlistDao().updateTrackList(
                playlistConvertor.toEntityTrackIdList(
                    removeTrackId(
                        currentPlalist.trackIdList,
                        id
                    )
                ), playlistKey
            )
            appDatabase.playlistDao()
                .updateNumberOfTracks(currentPlalist.numberOfTracks - 1, playlistKey)
        }
    }

    override suspend fun getTrackList(trackList: List<String>): List<Track> {
        val ids = trackList.mapNotNull { it.toIntOrNull() }
        val tracksEntity = appDatabase.localTrackDao().getTracksById(ids)
        return convertFromEntity(tracksEntity)
    }

    override suspend fun updateNumberofPlaylist(number: Int, playlistKey: Int) {
        appDatabase.playlistDao().updateNumberOfTracks(number, playlistKey)
    }

    override suspend fun getPlaylist(playlistKey: Int): Playlist {
        return convertFromPlaylistEntity(appDatabase.playlistDao().getPlaylistByKey(playlistKey))
    }

    private fun convertFromEntity(trackList: List<LocalTrackEntity>): List<Track> {
        return trackList.map { convertor.fromLocalTrackEntity(it) }
    }

    private fun convertFromPlaylistEntity(playlistEntity: PlaylistEntity): Playlist {
        return playlistEntity.let { playlistConvertor.map(it) }
    }

    private fun trackCounter(list: String, id: Int): Int {
        val regex = "\\b$id\\b"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(list)

        var count = 0
        while (matcher.find()) {
            count++
        }

        return count
    }

    private fun removeTrackId(list: List<String>, idToRemove: Int): List<String> {
        val newList = mutableListOf<String>()
        for (num in list) {
            if (num != idToRemove.toString()) {
                newList.add(num)
            }
        }
        Log.i("New playlist", newList.toString())
        return newList
    }

}