package com.example.playlistmaker.di

import android.media.MediaPlayer
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.playlistmaker.mediateka.data.AppDatabase
import com.example.playlistmaker.mediateka.data.FavoriteRepositoryImpl
import com.example.playlistmaker.mediateka.domain.FavoriteRepository
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
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppleServiceapit::class.java)
    }

    single {
        Gson()
    }

    single<NetworkClient> {
        NetworkClientImpl(appleServiceapit = get(), context = get())
    }

    factory { MediaPlayer() }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database.db"
        ).build()
    }

}