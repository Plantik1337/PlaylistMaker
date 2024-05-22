package com.example.playlistmaker.mediateka.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 3, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

}