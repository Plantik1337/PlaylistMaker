package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val THEME_KEY = "THEME_KEY"

class App : Application() {

    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        val sharedPreferences: SharedPreferences = getSharedPreferences(THEME_KEY, MODE_PRIVATE)
        when (sharedPreferences.getString(THEME_KEY, null)) {
            "MODE_NIGHT_YES" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            "MODE_NIGHT_NO" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(THEME_KEY, MODE_PRIVATE)
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                write(sharedPreferences, "MODE_NIGHT_YES")
                AppCompatDelegate.MODE_NIGHT_YES
                //Log.d("THEME","тёмная тема")
            } else {
                write(sharedPreferences, "MODE_NIGHT_NO")
                AppCompatDelegate.MODE_NIGHT_NO
                //Log.d("THEME","светлая тема")
            }
        )
    }

    fun isDarkMode(): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences(THEME_KEY, MODE_PRIVATE)
        return when (sharedPreferences.getString(THEME_KEY, MODE_PRIVATE.toString())) {
            "MODE_NIGHT_NO" -> false
            "MODE_NIGHT_YES" -> true
            else -> false
        }
    }

    private fun write(sharedPreferences: SharedPreferences, string: String) {
        sharedPreferences.edit()
            .putString(THEME_KEY, string)
            .apply()
    }
}
