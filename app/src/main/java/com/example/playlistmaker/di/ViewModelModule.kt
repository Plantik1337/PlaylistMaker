package com.example.playlistmaker.di

import com.example.playlistmaker.player.PlayerRepositoryImpl
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.logging.Handler

val viewModelModule = module {
    viewModel { (perviewUrl: String) ->
        PlayerViewModel(
            perviewUrl,
            player = PlayerRepositoryImpl(mediaPlayer = get())
        ) //PlayerRepository
    }
    viewModel {
        SettingsViewModel(get())
    }
    viewModel {
        SearchViewModel(get())
    }
}
