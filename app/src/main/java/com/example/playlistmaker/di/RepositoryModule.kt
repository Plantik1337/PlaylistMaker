package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.mediateka.data.AppDatabase
import com.example.playlistmaker.mediateka.data.FavoriteRepositoryImpl
import com.example.playlistmaker.mediateka.data.convertors.TrackDbConvertor
import com.example.playlistmaker.mediateka.domain.FavoriteInteractor
import com.example.playlistmaker.mediateka.domain.FavoriteInteractorImpl
import com.example.playlistmaker.mediateka.domain.FavoriteRepository
import com.example.playlistmaker.player.PlayerRepository
import com.example.playlistmaker.player.PlayerRepositoryImpl
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

val repositoryModule = module {

//    factory<PlayerRepository> {
//        PlayerRepositoryImpl(get())
//    }

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
    factory<TrackDbConvertor> { TrackDbConvertor() }

    single<FavoriteInteractor>{FavoriteInteractorImpl(favoriteRepository = get())}

    factory<FavoriteRepository> {
        FavoriteRepositoryImpl(appDatabase = get(), trackDbConvertor = get())
    }

}