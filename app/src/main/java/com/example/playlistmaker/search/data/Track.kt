package com.example.playlistmaker.search.data

import android.os.Parcel
import android.os.Parcelable

data class MusicResponse(
    val resultCount: String, val results: List<Track>
)

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val previewUrl: String,
    val releaseDate: String,
    val country: String,
    val primaryGenreName: String,
    val collectionName: String,
    val collectionExplicitness: String,
) : Parcelable {
    constructor(
        parcel: Parcel
    ) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeString(trackTimeMillis)
        parcel.writeString(artworkUrl100)
        parcel.writeString(previewUrl)
        parcel.writeString(releaseDate)
        parcel.writeString(country)
        parcel.writeString(primaryGenreName)
        parcel.writeString(collectionName)
        parcel.writeString(collectionExplicitness)
    }
    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel) = Track(parcel)
        override fun newArray(size: Int) = arrayOfNulls<Track>(size)
    }
}