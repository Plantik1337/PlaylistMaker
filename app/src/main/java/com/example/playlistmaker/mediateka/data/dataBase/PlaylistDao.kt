package com.example.playlistmaker.mediateka.data.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.mediateka.data.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun newPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("DELETE FROM playlists_table WHERE `key` =:playlistId")
    suspend fun deletPlaylistByKey(playlistId: Int)

    @Query("UPDATE playlists_table SET trackIdList =:newTrackString  WHERE `key` = :playlistId")
    suspend fun updateTrackList(newTrackString: String, playlistId: Int)

    @Query("UPDATE playlists_table SET numberOfTracks = :numberOfTracks WHERE `key` = :playlistId")
    suspend fun updateNumberOfTracks(numberOfTracks: Int, playlistId: Int)

    @Query("SELECT trackIdList FROM playlists_table WHERE `key` =:playlistKey")
    suspend fun isExistsInPlaylist(playlistKey: Int): String
}