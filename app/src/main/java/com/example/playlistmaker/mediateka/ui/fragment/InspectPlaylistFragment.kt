package com.example.playlistmaker.mediateka.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ViewPlaylistFragmentBinding
import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.mediateka.ui.MediatekaAdapter
import com.example.playlistmaker.mediateka.ui.RecyclerViewClickListener
import com.example.playlistmaker.mediateka.ui.viewmodel.InspectPlaylistViewModel
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.data.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class InspectPlaylistFragment : Fragment() {
    private lateinit var binding: ViewPlaylistFragmentBinding

    private lateinit var actualTrackList: MutableList<Track>

    private lateinit var actualPlaylist: Playlist

    companion object {
        const val PLAYLIST = "PLAYLIST"

        var canShare = false

        fun createArgs(playlist: Playlist): Bundle = bundleOf(
            PLAYLIST to playlist
        )
    }

    private val viewModel: InspectPlaylistViewModel by viewModel {
        parametersOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = ViewPlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actualTrackList = mutableListOf()

        val currentPlaylist: Playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(PLAYLIST, Playlist::class.java)!!
        } else {
            @Suppress("DEPRECATION") requireArguments().getParcelable<Playlist>(PLAYLIST)!!
        }

        actualPlaylist = currentPlaylist

        viewModel.playlistLiveData().observe(viewLifecycleOwner) { newCurrentPlaylist ->
            actualPlaylist = newCurrentPlaylist
            Log.i("Actual Playlist", actualPlaylist.trackIdList.toString())
            if (newCurrentPlaylist.imageURI?.isNotBlank() == true) {
                if (newCurrentPlaylist.numberOfTracks > 0) {
                    canShare = true

                } else {
                    canShare = false

                }
                val imageFile = File(newCurrentPlaylist.imageURI)
                if (imageFile.exists()) {
                    Glide.with(this).load(imageFile).into(binding.albumImageView)
                    Glide.with(this).load(imageFile).into(binding.playlistiv)
                }
            }
            binding.playlistNametv.text = newCurrentPlaylist.playlistName
            binding.playlistName.text = newCurrentPlaylist.playlistName
            binding.description.text = newCurrentPlaylist.description
            viewModel.getTrackList(actualPlaylist.trackIdList)
        }
        viewModel.getPlaylist(currentPlaylist.key)


        val bottomSheetContainer = binding.bottomSheet
        val bottomSheetExtraOptionContainer = binding.bottomSheetExtraOption

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val bottomSheetExtraOptionBehavior =
            BottomSheetBehavior.from(bottomSheetExtraOptionContainer).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        binding.playlistrv.layoutManager = LinearLayoutManager(requireContext())

        viewModel.trackLsitLiveData().observe(viewLifecycleOwner) {
            Log.i("плейлист", it.toString())
            actualTrackList.clear()
            actualTrackList.addAll(it)
            binding.playlistrv.adapter = recyclerViewInteractor(it, actualPlaylist)
            binding.totalTime.text = getTotalTime(it)
            if (it.isEmpty()) {
                canShare = false
                binding.playlistrv.isVisible = false
                binding.emptyListMessage.isVisible = true
            } else {
                canShare = true
                binding.playlistrv.isVisible = true
                binding.emptyListMessage.isVisible = false
            }
        }
        viewModel.getTrackList(currentPlaylist.trackIdList)



        binding.openExpandOptions.setOnClickListener {
            bottomSheetExtraOptionBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.shareButton.setOnClickListener {
            shareIntent()
        }
        binding.shareBottomSheetButton.setOnClickListener {
            shareIntent()
            bottomSheetExtraOptionBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.deletePlaylistButton.setOnClickListener {
            bottomSheetExtraOptionBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            AlertDialog.Builder(requireContext()).setTitle("Удалить плейлист")
                .setMessage("Хотите удалить плейлист?").setPositiveButton("Да") { _, _ ->
                    viewModel.deleteTracksAndPlaylist(
                        actualPlaylist.trackIdList, currentPlaylist.key
                    )
                    findNavController().navigateUp()
                }.setNegativeButton("Нет", null).show()
        }

        binding.editPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_inspectPlaylistFragment_to_createPlaylistFragment,
                CreatePlaylistFragment.createArg(actualPlaylist)
            )
        }
        val totalTracks = getTotalNumberOfTracks(actualPlaylist.numberOfTracks)
        binding.totalTracks.text = totalTracks
        binding.numberOfTracks.text = totalTracks

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }


        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        bottomSheetExtraOptionBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })


    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun recyclerViewInteractor(tracks: List<Track>, playlist: Playlist): MediatekaAdapter {
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
                deleteTrack(tracks[position], tracks, playlist)
                return true
            }


        })
        return adapter
    }

    private fun deleteTrack(trackToDelete: Track, tracks: List<Track>, playlist: Playlist) {
        if (isAdded) {
            AlertDialog.Builder(requireContext()).setTitle("Удалить трек")
                .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
                .setPositiveButton("Да") { _, _ ->

                    val updatedList = tracks.toMutableList()
                    updatedList.remove(trackToDelete)
                    val numericOnly = binding.totalTracks.text.replace(Regex("[^0-9]"), "").toInt()
                    val newTotalTracks = if (numericOnly == 0) {
                        numericOnly
                    } else {
                        numericOnly - 1
                    }
                    Log.i("новая и старая цифра", "${newTotalTracks}, $numericOnly")
                    val newStingList = mutableListOf<String>()
                    updatedList.forEach {
                        newStingList.add(it.trackId.toString())
                    }
                    Log.i("новая строка", newStingList.toString())
                    viewModel.getTrackList(newStingList)

                    val totaltrack = getTotalNumberOfTracks(newTotalTracks)
                    binding.totalTracks.text = totaltrack
                    binding.numberOfTracks.text = totaltrack
                    binding.totalTime.text = getTotalTime(updatedList)
                    actualTrackList.remove(trackToDelete)

                    //Удаляем трек
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            viewModel.deleteTrack(trackToDelete.trackId, playlistKey = playlist.key)
                        }
                    }

                }.setNegativeButton("Нет", null).show()
        }
        viewModel.getPlaylist(playlist.key)
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

    private fun shareIntent() {
        if (canShare) {
            val stringTosend: String =
                "${binding.playlistName.text}\n" + "${binding.description.text}\n" + "${binding.totalTracks.text} " + "${binding.totalTime.text}\n" + "${
                    tracklistToString(actualTrackList)
                }\n"
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, stringTosend)
                type = "text/plain"

            }
            val intent = Intent.createChooser(sendIntent, stringTosend)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            requireContext().startActivity(intent)
        } else {
            Toast.makeText(
                requireContext(), "Вы не можете поделиться пустым плейлистом", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun tracklistToString(list: List<Track>): String {
        var counter = 1
        var prumaryString = ""
        list.forEach {
            prumaryString += "${counter} - ${it.artistName} - ${it.trackName} (${
                SimpleDateFormat(
                    "mm:ss", Locale.getDefault()
                ).format(it.trackTimeMillis.toLong())
            })\n"
            counter++
        }
        return prumaryString
    }
}