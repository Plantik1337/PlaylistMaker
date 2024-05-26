package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.ui.viewmodel.CreatePlaylistViewModel
import com.example.playlistmaker.mediateka.ui.viewmodel.FavoriteViewModel
import com.example.playlistmaker.mediateka.ui.viewmodel.PlaylistViewModel
import com.example.playlistmaker.player.domain.PlayerRepositoryImpl
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (trackId: Int, previewUrl: String) ->
        PlayerViewModel(
            trackId = trackId,
            previewUrl = previewUrl,
            player = get()
        )//Player
    }
    viewModel {
        SettingsViewModel(
            settingsInteractor = get()
        )//Settings
    }
    viewModel {
        SearchViewModel(
            interactor = get()
        )//Search
    }
    viewModel {
        PlaylistViewModel(
            interactor = get()
        )//Playlist
    }
    viewModel {
        FavoriteViewModel(
            interactor = get()
        )//Favorite
    }
    viewModel {
        CreatePlaylistViewModel(
            interactor = get()
        )//CreatePlaylist
    }

}
