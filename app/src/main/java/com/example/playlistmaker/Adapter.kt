package com.example.playlistmaker

import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter(
    private val track: ArrayList<Track>
) : RecyclerView.Adapter<Adapter.TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.v_track_line, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])
    }

    override fun getItemCount() = track.size

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val albumImage: ImageView = itemView.findViewById(R.id.albumImageView)
        private val trackName: TextView = itemView.findViewById(R.id.trackNameTextView)
        private val artistName: TextView = itemView.findViewById(R.id.artistNameTextView)
        private val songDuration: TextView = itemView.findViewById(R.id.songDurationTextView)

//        fun getScreenWidthInDp(context: Context): Int {// размер экрана
//            val displayMetrics = context.resources.displayMetrics
//            return (displayMetrics.widthPixels / displayMetrics.density).toInt()
//        }
//
//        fun shortenText(text: String, maxLength: Int): String {
//            return if (text.length > maxLength - 110){
//                val shortenText = text.substring(0,maxLength)
//                shortenText
//            }else{
//                text
//            }
//        }

        fun bind(track: Track) {

            Glide.with(itemView).load(track.artworkUrl100).into(albumImage)
            albumImage.clipToOutline = true
            albumImage.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    val cornerRadius = 15
                    outline.setRoundRect(0, 0, view.width, view.height, cornerRadius.toFloat())
                }
            }
            val maxSymbols = 32
            if (track.trackName.length > maxSymbols) {
                trackName.text = track.trackName.substring(0, maxSymbols) + "..."
            } else {
                trackName.text = track.trackName
            }
            artistName.text = track.artistName
            songDuration.text = track.trackTime

        }
    }
}

