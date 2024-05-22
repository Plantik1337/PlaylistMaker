package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.mediateka.data.CreatePlaylistRepositoryImpl
import com.example.playlistmaker.mediateka.data.FavoriteRepositoryImpl
import com.example.playlistmaker.mediateka.data.PlaylistRepository
import com.example.playlistmaker.mediateka.data.PlaylistRepositoryImpl
import com.example.playlistmaker.mediateka.data.convertors.TrackDbConvertor
import com.example.playlistmaker.mediateka.domain.CreatePlaylistInteractor
import com.example.playlistmaker.mediateka.domain.CreatePlaylistInteractorImpl
import com.example.playlistmaker.mediateka.domain.CreatePlaylistRepository
import com.example.playlistmaker.mediateka.domain.FavoriteInteractor
import com.example.playlistmaker.mediateka.domain.FavoriteInteractorImpl
import com.example.playlistmaker.mediateka.domain.FavoriteRepository
import com.example.playlistmaker.mediateka.domain.PlaylistInteractor
import com.example.playlistmaker.mediateka.domain.PlaylistInteractorImpl
import com.example.playlistmaker.player.data.PlayerDatabaseRepository
import com.example.playlistmaker.player.data.PlayerDatabaseRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.HistoryTransaction
import com.example.playlistmaker.search.domain.HistoryRepository
import com.example.playlistmaker.search.domain.Interactor
import com.example.playlistmaker.search.domain.InteractorImlp
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.Impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.math.sin

val repositoryModule = module {

    factory<SettingsInteractor> {
        SettingsInteractorImpl(
            context = get(), repository = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(context = get())
    }

    single<Interactor> {
        InteractorImlp(history = get(), networkClient = get())
    }
    single<HistoryRepository> {
        HistoryTransaction(
            sharedPreferences = androidContext()
                .getSharedPreferences("HISTORY_LIST", Context.MODE_PRIVATE),
            gson = get()
        )
    }


    single<FavoriteInteractor> { FavoriteInteractorImpl(favoriteRepository = get()) }

    factory<FavoriteRepository> {
        FavoriteRepositoryImpl(
            appDatabase = get(),
            trackDbConvertor = get()
        )
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(
            mediaPlayer = get(),
            playerDatabaseRepository = get()
        )
    }

    factory<PlayerDatabaseRepository> {
        PlayerDatabaseRepositoryImpl(
            appDatabase = get(),
            trackDbConvertor = get()
        )
    }

    single<CreatePlaylistInteractor> {
        CreatePlaylistInteractorImpl(
            createPlaylistRepository = get()
        )
    }
    factory<CreatePlaylistRepository> {
        CreatePlaylistRepositoryImpl(
            appDatabase = get(),
            convertor = get()
        )
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(
            repository = get()
        )
    }

    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(
            appDatabase = get(),
            convertor = get()
        )
    }

}