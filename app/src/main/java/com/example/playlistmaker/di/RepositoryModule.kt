package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.player.PlayerRepository
import com.example.playlistmaker.player.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.HistoryTransaction
import com.example.playlistmaker.search.domain.HistoryRepository
import com.example.playlistmaker.search.domain.Interactor
import com.example.playlistmaker.search.domain.InteractorImlp
import com.example.playlistmaker.settings.domain.Impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val repositoryModule = module {

    factory<PlayerRepository> {(mediaPlayer: MediaPlayer) ->
        PlayerRepositoryImpl(mediaPlayer) }
    //factory { MediaPlayer() }

    factory<SettingsInteractor> { SettingsInteractorImpl() }

    single<Interactor> {
        InteractorImlp(history = get(), networkClient = get())
    }
    single<HistoryRepository> {
        HistoryTransaction(
            sharedPreferences = androidContext()
                .getSharedPreferences("HISTORY_LIST", Context.MODE_PRIVATE),
            gson = Gson()
        )
    }
}