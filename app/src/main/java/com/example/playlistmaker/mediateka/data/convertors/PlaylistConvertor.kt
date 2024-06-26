package com.example.playlistmaker.mediateka.data.convertors

import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.mediateka.data.dataBase.PlaylistEntity
import com.example.playlistmaker.search.data.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistConvertor(private val gson: Gson) {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            key = playlist.key,
            playlistName = playlist.playlistName,
            description = if (playlist.description.isNullOrBlank()) {
                ""
            } else {
                playlist.description
            },
            imageURI = if (playlist.imageURI.isNullOrBlank()) {
                ""
            } else {
                playlist.imageURI
            },
            trackIdList = toEntityTrackIdList(playlist.trackIdList),
            numberOfTracks = playlist.trackIdList.size
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            key = playlistEntity.key,
            playlistName = playlistEntity.playlistName,
            description = playlistEntity.description,
            imageURI = playlistEntity.imageURI,
            trackIdList = fromEntityTrackIdList(playlistEntity.trackIdList),
            numberOfTracks = playlistEntity.numberOfTracks
        )
    }

    fun toEntityTrackIdList(trackIdList: List<String>): String {
        return gson.toJson(trackIdList)
    }

    fun fromEntityTrackIdList(trackIdListString: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(trackIdListString, listType)
    }
}