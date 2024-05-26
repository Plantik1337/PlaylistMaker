package com.example.playlistmaker.mediateka.ui

interface RecyclerViewClickListener {
    fun onItemClick(position: Int)
    fun onItemLongClick(position: Int): Boolean
}