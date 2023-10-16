package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.Track
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
            myList
        }
    }

    fun write(sharedPreferences: SharedPreferences, track: Track) {
        val first = 0
        val maxListSize = 11
        var myJson = sharedPreferences.getString(HISTORY_LIST, null)
        val myList: ArrayList<Track> = if (myJson.isNullOrBlank()) {
            arrayListOf()
        } else {
            Gson().fromJson(myJson, object : TypeToken<ArrayList<Track>>() {}.type)
        }
        myList.remove(track)
        myList.add(first, track)
        if (myList.size == maxListSize) {
            myList.removeAt(maxListSize - 1)
        }
        myJson = Gson().toJson(myList)
        sharedPreferences.edit()
            .putString(HISTORY_LIST, myJson)
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
    fun returnFirst(sharedPreferences: SharedPreferences):Track{
        val listOfData = read(sharedPreferences)
        return listOfData[0]
    }
}