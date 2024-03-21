package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.search.data.network.AppleServiceapit
import com.example.playlistmaker.search.data.network.NetworkClientImpl
import com.example.playlistmaker.search.data.network.NetworkClient
import com.google.gson.Gson
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
        Gson() }

    single<NetworkClient> {
        NetworkClientImpl(appleServiceapit = get(), context = get()) }

    factory { MediaPlayer() }
}