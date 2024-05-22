package com.example.playlistmaker.mediateka.data

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun newPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("UPDATE playlists_table SET trackIdList =:newTrackString  WHERE `key` = :playlistId")
    suspend fun updateTrackList(newTrackString: String, playlistId: Int)
}