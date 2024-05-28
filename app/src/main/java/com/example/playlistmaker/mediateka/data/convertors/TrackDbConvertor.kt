package com.example.playlistmaker.mediateka.data.convertors

import com.example.playlistmaker.mediateka.data.dataBase.LocalTrackEntity
import com.example.playlistmaker.mediateka.data.dataBase.TrackEntity
import com.example.playlistmaker.search.data.Track

class TrackDbConvertor {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            id = track.trackId,
            addTime = System.currentTimeMillis(),
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            releaseDate = track.releaseDate,
            country = track.country,
            collectionName = track.collectionName,
            collectionExplicitness = track.collectionExplicitness,
            primaryGenreName = track.primaryGenreName
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            trackId = track.id,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            releaseDate = track.releaseDate,
            country = track.country,
            collectionName = track.collectionName,
            collectionExplicitness = track.collectionExplicitness,
            primaryGenreName = track.primaryGenreName
        )
    }

    fun fromLocalTrackEntity(track: LocalTrackEntity): Track {
        return Track(
            trackId = track.id,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            releaseDate = track.releaseDate,
            country = track.country,
            collectionName = track.collectionName,
            collectionExplicitness = track.collectionExplicitness,
            primaryGenreName = track.primaryGenreName
        )
    }

    fun toLocalTrackEntity(track: Track): LocalTrackEntity {
        return LocalTrackEntity(
            id = track.trackId,
            addTime = System.currentTimeMillis(),
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            releaseDate = track.releaseDate,
            country = track.country,
            collectionName = track.collectionName,
            collectionExplicitness = track.collectionExplicitness,
            primaryGenreName = track.primaryGenreName
        )
    }
}