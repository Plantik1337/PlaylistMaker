package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.viewmodel.FavoriteViewModel
import com.example.playlistmaker.mediateka.viewmodel.PlaylistViewModel
import com.example.playlistmaker.player.PlayerRepositoryImpl
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (previewUrl: String) ->
        PlayerViewModel(
            previewUrl = previewUrl,
            player = PlayerRepositoryImpl(
                mediaPlayer = get(),
                favoriteRepository = get()
            )
        ) //PlayerRepository
    }
    viewModel {
        SettingsViewModel(get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel { (data: String) ->
        PlaylistViewModel(data)
    }
    viewModel {
        FavoriteViewModel()
    }

}
