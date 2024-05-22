package com.example.playlistmaker.di

import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.mediateka.data.dataBase.AppDatabase
import com.example.playlistmaker.mediateka.data.convertors.PlaylistConvertor
import com.example.playlistmaker.mediateka.data.convertors.TrackDbConvertor
import com.example.playlistmaker.search.data.network.AppleServiceapit
import com.example.playlistmaker.search.data.network.NetworkClientImpl
import com.example.playlistmaker.search.data.network.NetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModuel = module {

    single<AppleServiceapit> {
        Retrofit.Builder().baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(AppleServiceapit::class.java)
    }

    single {
        Gson()
    }

    single<NetworkClient> {
        NetworkClientImpl(appleServiceapit = get(), context = get())
    }

    factory<TrackDbConvertor> { TrackDbConvertor() }

    factory<PlaylistConvertor> { PlaylistConvertor(gson = get()) }

    factory { MediaPlayer() }

    single {
        Room.databaseBuilder(
            androidContext(), AppDatabase::class.java, "database.db"
        ).fallbackToDestructiveMigration().build()
    }

}