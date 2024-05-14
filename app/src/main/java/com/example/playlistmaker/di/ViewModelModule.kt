package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.viewmodel.FavoriteViewModel
import com.example.playlistmaker.mediateka.viewmodel.PlaylistViewModel
import com.example.playlistmaker.player.domain.PlayerRepositoryImpl
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (previewUrl: String) ->
        PlayerViewModel(
            previewUrl = previewUrl, player = get()
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
    viewModel { (data: String) ->
        PlaylistViewModel(
            data = data
        )//Playlist in progress
    }
    viewModel {
        FavoriteViewModel(
            interactor = get()
        )//Favorite
    }

}
