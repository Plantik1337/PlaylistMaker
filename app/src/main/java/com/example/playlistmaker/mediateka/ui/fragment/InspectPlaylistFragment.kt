package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ViewPlaylistFragmentBinding
import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.mediateka.ui.MediatekaAdapter
import com.example.playlistmaker.mediateka.ui.RecyclerViewClickListener
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.data.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class InspectPlaylistFragment : Fragment() {
    private lateinit var binding: ViewPlaylistFragmentBinding

    companion object {
        const val PLAYLIST = "PLAYLIST"
        fun createArgs(playlist: Playlist): Bundle = bundleOf(
            PLAYLIST to playlist
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = ViewPlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentPlaylist: Playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(PLAYLIST, Playlist::class.java)!!
        } else {
            @Suppress("DEPRECATION") requireArguments().getParcelable<Playlist>(PLAYLIST)!!
        }

        if (currentPlaylist.imageURI?.isNotBlank() == true) {
            val imageFile = File(currentPlaylist.imageURI)
            if (imageFile.exists()) {
                Glide.with(this)
                    .load(imageFile)
                    .into(binding.albumImageView)
            }
        }

        val bottomSheetContainer = binding.bottomSheet

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.playlistName.text = currentPlaylist.playlistName
        binding.description.text = currentPlaylist.description

        binding.playlistrv.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistrv.adapter = recyclerViewInteractor(currentPlaylist.trackIdList)

        binding.totalTime.text = getTotalTime(tracks = currentPlaylist.trackIdList)
        binding.totalTracks.text = getTotalNumberOfTracks(currentPlaylist.numberOfTracks)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }

                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })


    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun recyclerViewInteractor(tracks: List<Track>): MediatekaAdapter {
        val adapter = MediatekaAdapter(tracks, object : RecyclerViewClickListener {

            override fun onItemClick(position: Int) {
                findNavController().navigate(
                    R.id.action_inspectPlaylistFragment_to_playerFragment,
                    PlayerFragment.createArgs(
                        track = tracks[position]
                    )
                )
            }

            override fun onItemLongClick(position: Int): Boolean {
                Toast.makeText(
                    requireContext(),
                    "Long clicked item at position $position",
                    Toast.LENGTH_LONG
                ).show()
                return true
            }
        })
        return adapter
    }

    private fun getTotalTime(tracks: List<Track>): String {
        var milliseconds: Long = 0
        tracks.forEach {
            milliseconds += it.trackTimeMillis.toLong()
        }
        val minutes = SimpleDateFormat("mm", Locale.getDefault()).format(milliseconds).toInt()

        val lastDigit = minutes % 10
        val lastTwoDigits = minutes % 100

        val minuteWord = when {
            lastTwoDigits in 11..14 -> "минут"
            lastDigit == 1 -> "минута"
            lastDigit in 2..4 -> "минуты"
            else -> "минут"
        }

        return "${minutes} ${minuteWord}"
    }

    private fun getTotalNumberOfTracks(number: Int): String {
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