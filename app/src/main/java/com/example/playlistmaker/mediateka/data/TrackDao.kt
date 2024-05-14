package com.example.playlistmaker.mediateka.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("DELETE FROM track_table WHERE id = :trackId")
    suspend fun deleteById(trackId:Int)

    //@Query("SELECT * FROM track_table WHERE EXISTS(SELECT * FROM track_table WHERE id = :trackId)")
    @Query("SELECT EXISTS(SELECT 1 FROM track_table WHERE id = :trackId)")
    suspend fun isExists(trackId: Int): Boolean
}