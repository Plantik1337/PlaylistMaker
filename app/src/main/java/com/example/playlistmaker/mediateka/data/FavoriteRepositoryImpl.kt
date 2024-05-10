package com.example.playlistmaker.mediateka.data

import com.example.playlistmaker.mediateka.data.convertors.TrackDbConvertor
import com.example.playlistmaker.mediateka.domain.FavoriteRepository
import com.example.playlistmaker.search.data.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteRepository {
    override fun favoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromEntity(tracks))
    }

//    override fun isFavorite(trackId: Int): Flow<Boolean> = flow {
//        val isfavorite = appDatabase.trackDao().checkTrackExists(trackId)
//        emit(
//            when {
//                isfavorite > 0 -> true
//                else -> false
//            }
//        )
//    }

     override suspend fun likeTrack(track: Track)  {
        appDatabase.trackDao().insertTrack(convertToEntity(track))
        //appDatabase.trackDao().insertTrack()
//        flow<Track> {
//            appDatabase.trackDao().insertTrack(convertToEntity(track))
//        }
    }

    override suspend fun deleteTrack(trackId: Int) {
//        flow<Int> {
//            appDatabase.trackDao().deleteTrackById(trackId)
//        }
        appDatabase.trackDao().deleteById(trackId)
    }

    override suspend fun isExists(trackId: Int): Boolean {
        return appDatabase.trackDao().isExists(trackId)
    }

    private fun convertToEntity(track: Track): TrackEntity{
        return track.let {trackDbConvertor.map(track) }
    }

    private fun convertFromEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}