package com.example.playlistmaker.mediateka.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 4,
    entities = [TrackEntity::class, PlaylistEntity::class, LocalTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun localTrackDao(): LocalTrackDao
}