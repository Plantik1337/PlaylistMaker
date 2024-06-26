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
import com.example.playlistmaker.search.data.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MediatekaAdapter(
    private var track: List<Track>,
    private val clickListener: RecyclerViewClickListener
) : RecyclerView.Adapter<MediatekaAdapter.TrackViewHolder>() {

    fun getTrackList(): List<Track> {
        return track
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.v_track_line, parent, false)
        return TrackViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])
    }

    override fun getItemCount(): Int = track.size

    class TrackViewHolder(itemView: View, private val clickListener: RecyclerViewClickListener) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        private val albumImage: ImageView = itemView.findViewById(R.id.albumImageView)
        private val trackName: TextView = itemView.findViewById(R.id.trackNameTextView)
        private val artistName: TextView = itemView.findViewById(R.id.artistNameTextView)
        private val songDuration: TextView = itemView.findViewById(R.id.songDurationTextView)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                clickListener.onItemClick(position)
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            val position = adapterPosition
            return if (position != RecyclerView.NO_POSITION) {
                clickListener.onItemLongClick(position)
            } else {
                false
            }
        }

        fun bind(track: Track) {
            val maxSymbols = 30

            trackName.text = track.trackName

            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(albumImage)
            albumImage.clipToOutline = true
            albumImage.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    val cornerRadius = 15
                    outline.setRoundRect(0, 0, view.width, view.height, cornerRadius.toFloat())
                }
            }

            artistName.text = track.artistName

            if (track.artistName.length > maxSymbols) {
                artistName.text = track.artistName.substring(0, maxSymbols) + "..."
            } else {
                artistName.text = track.artistName
            }

            songDuration.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis.toLong())

        }


    }
}