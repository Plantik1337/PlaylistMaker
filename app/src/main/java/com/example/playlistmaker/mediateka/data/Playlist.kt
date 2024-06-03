package com.example.playlistmaker.mediateka.data

import android.os.Parcel
import android.os.Parcelable
import com.example.playlistmaker.search.data.Track

data class Playlist(
    val key: Int,
    val playlistName: String,
    val description: String?,
    val imageURI: String?,
    val trackIdList: List<String>,
    val numberOfTracks: Int

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readInt()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(key)
        parcel.writeString(playlistName)
        parcel.writeString(description)
        parcel.writeString(imageURI)
        parcel.writeStringList(trackIdList)
        parcel.writeInt(numberOfTracks)
    }

    companion object CREATOR : Parcelable.Creator<Playlist> {
        override fun createFromParcel(parcel: Parcel): Playlist {
            return Playlist(parcel)
        }

        override fun newArray(size: Int): Array<Playlist?> {
            return arrayOfNulls(size)
        }
    }
}
