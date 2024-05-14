package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.search.data.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun favoriteTracks(): Flow<List<Track>>
}