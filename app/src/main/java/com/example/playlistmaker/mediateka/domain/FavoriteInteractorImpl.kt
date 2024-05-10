package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.search.data.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository): FavoriteInteractor {
    override fun favotiteTracks(): Flow<List<Track>> {
        return favoriteRepository.favoriteTracks()
    }

}