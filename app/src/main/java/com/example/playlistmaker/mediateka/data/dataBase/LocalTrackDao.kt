package com.example.playlistmaker.mediateka.data.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocalTrackDao {
    @Insert(entity = LocalTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(localTrackEntity: LocalTrackEntity)

    @Query("SELECT * FROM local_tracks_table WHERE id IN(:ids) ORDER BY addTime DESC")
    suspend fun getTracksById(ids: List<Int>): List<LocalTrackEntity>

    @Query("DELETE FROM local_tracks_table WHERE id = :trackId")
    suspend fun deleteById(trackId:Int)

    @Query("SELECT EXISTS(SELECT 1 FROM local_tracks_table WHERE id = :trackId)")
    suspend fun isExists(trackId: Int): Boolean
}