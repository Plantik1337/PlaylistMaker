package com.example.playlistmaker.mediateka.ui

import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateka.data.Playlist
import java.io.File

class PlaylistAdapter(
    private var playlists: List<Playlist>,
    private val clickListener: RecyclerViewClickListener
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.v_playlist_element, parent, false)
        return PlaylistViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size


    class PlaylistViewHolder(itemView: View, private val clickListener: RecyclerViewClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val albumImage: ImageView = itemView.findViewById(R.id.playlistimageView)
        private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
        private val numberOfTracks: TextView = itemView.findViewById(R.id.numberOfTracks)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                clickListener.onItemClick(position)
            }
        }

        fun bind(playlist: Playlist) {
            if (playlist.imageURI?.isNotBlank() == true) {
                val imageFile = File(playlist.imageURI)
                if (imageFile.exists()) {

                    Glide.with(itemView)
                        .load(imageFile)
                        .into(albumImage)

                    albumImage.clipToOutline = true
                    albumImage.outlineProvider = object : ViewOutlineProvider() {
                        override fun getOutline(view: View, outline: Outline) {
                            val cornerRadius = 15
                            outline.setRoundRect(
                                0,
                                0,
                                view.width,
                                view.height,
                                cornerRadius.toFloat()
                            )
                        }
                    }
                }
            }
            playlistName.text = playlist.playlistName

            numberOfTracks.text = getTracksString(playlist.numberOfTracks)
        }

        private fun getTracksString(number: Int): String {
            val n = number % 100
            return when {
                n in 11..19 -> "${number} треков"
                else -> when (n % 10) {
                    1 -> "${number} трек"
                    in 2..4 -> "${number} трека"
                    else -> "${number} треков"
                }
            }
        }
    }
}

