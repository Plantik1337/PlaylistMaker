package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.HistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val HISTORY_LIST = "HISTORY_LIST"


class HistoryTransaction(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : HistoryRepository {

    private val emptyArrayList = ArrayList<Track>()

    override fun read(): ArrayList<Track> {

        val json = sharedPreferences.getString(HISTORY_LIST, null)
        var myList = emptyArrayList
        return if (json.isNullOrBlank()) {
            myList
        } else {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            myList = gson.fromJson(json, type)
            myList
        }
    }

    override fun write(track: Track) {
        val first = 0
        val maxListSize = 11
        var myJson = sharedPreferences.getString(HISTORY_LIST, null)
        val myList: ArrayList<Track> = if (myJson.isNullOrBlank()) {
            arrayListOf()
        } else {
            gson.fromJson(myJson, object : TypeToken<ArrayList<Track>>() {}.type)
        }
        myList.remove(track)
        myList.add(first, track)
        if (myList.size == maxListSize) {
            myList.removeAt(maxListSize - 1)
        }
        myJson = gson.toJson(myList)
        sharedPreferences.edit()
            .putString(HISTORY_LIST, myJson)
            .apply()
    }

    override fun clearHistory() {
        sharedPreferences.edit().clear().apply()
    }

    fun historyIsEmpty(): Boolean {
        val myJson = sharedPreferences.getString(HISTORY_LIST, null)
        return when (myJson == null) {
            true -> true
            false -> false
        }
    }

    override fun returnFirst(): Track {
        val listOfData = read()
        return listOfData[0]
    }
}