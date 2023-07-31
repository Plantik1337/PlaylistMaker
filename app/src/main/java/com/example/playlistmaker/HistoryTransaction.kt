package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val HISTORY_LIST = "HISTORY_LIST"

class HistoryTransaction {
    private val emptyArrayList = ArrayList<Track>()
    fun read(sharedPreferences: SharedPreferences): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY_LIST, null)
        var myList = emptyArrayList
        return if (json.isNullOrBlank()) {
            myList
        } else {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            myList = Gson().fromJson(json, type)
            myList.reverse()
            myList
        }
    }

    fun write(sharedPreferences: SharedPreferences, track: Track) {
        val myJson = sharedPreferences.getString(HISTORY_LIST, null)
        var myList = emptyArrayList
        if (myJson.isNullOrBlank()) {
            myList.add(track)
        } else {
            myList = Gson().fromJson(myJson, ArrayList<Track>()::class.java)
            myList.add(track)
        }
        myList = ArrayList(myList.distinct())
        //myList = ArrayList(myList.dropLast(0))
        val newJson = Gson().toJson(myList)
        sharedPreferences.edit()
            .putString(HISTORY_LIST, newJson)
            .apply()
    }

    fun cleanHistory(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().clear().apply()
    }

    fun historyIsEmpty(sharedPreferences: SharedPreferences): Boolean {
        val myJson = sharedPreferences.getString(HISTORY_LIST, null)
        return when (myJson == null) {
            true -> true
            false -> false
        }
    }
}