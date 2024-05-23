package com.example.playlistmaker.player.ui

import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.mediateka.ui.RecyclerViewClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding

    companion object {

        const val TRACK_KEY = "TRACK_KEY"

        fun createArgs(track: Track): Bundle = bundleOf(
            TRACK_KEY to track
        )
    }

    private val viewModel: PlayerViewModel by viewModel {
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(TRACK_KEY, Track::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable(TRACK_KEY) as Track?
        }
        track?.let {
            parametersOf(it.trackId, it.previewUrl)
        } ?: throw IllegalArgumentException("Track cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentContent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(TRACK_KEY, Track::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable<Track>(TRACK_KEY)!!
        }

        binding.playlistrv.layoutManager = LinearLayoutManager(requireContext())

        viewModel.isTrackLiked()

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.isExists(currentContent.trackId)
        }

        fun recyclerViewInteractor(playlists: List<Playlist>): PlayerPlaylistAdapter {
            val adapter = PlayerPlaylistAdapter(playlists, object : RecyclerViewClickListener {
                override fun onItemClick(position: Int) {
                    viewModel.isExistsInPlaylist(
                        playlists[position].key,
                        trackId = currentContent.trackId.toString()
                    )
                }
            })
            return adapter
        }

        viewModel.isTrackLiked().observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    //ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
                    Log.i("Нажатие", "Обсервер нашёлся, должен быть красным")
                    binding.likeButton.drawable.setTint(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.heartRed
                        )
                    )
                }

                false -> {
                    Log.i("Нажатие", "Обсервер нашёлся, должен быть серым")
                    binding.likeButton.drawable.setTint(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey
                        )
                    )
                }
            }
        }

        val play = binding.playPauseButton

        binding.songNamePlayer.text = currentContent.trackName
        binding.autorName.text = currentContent.artistName

        Glide.with(binding.albumPlayerImageView)
            .load(currentContent.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
            .into(binding.albumPlayerImageView)
        binding.albumPlayerImageView.clipToOutline = true
        binding.albumPlayerImageView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val cornerRadius = 15
                outline.setRoundRect(0, 0, view.width, view.height, cornerRadius.toFloat())
            }
        }

        viewModel.playerLiveData().observe(viewLifecycleOwner) {
            play.isEnabled = it.isPlayButtonEnabled

            binding.trackTimeView.text = it.progress

            when (it.buttonText) {
                "PLAY" -> {
                    binding.playPauseButton.setImageResource(R.drawable.baseline_play_circle_24)
                }

                else -> {
                    binding.playPauseButton.setImageResource((R.drawable.baseline_pause_circle_24))
                }
            }
        }

        viewModel.playlistLiveData().observe(viewLifecycleOwner) {
            Log.i("список плейлистов", "${it}")
            binding.playlistrv.adapter = recyclerViewInteractor(it)
        }

        play.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.playerSongDurationTextViewData.text = SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(currentContent.trackTimeMillis.toLong())

        if (currentContent.collectionName.contains("single")) {
            binding.playerAlbumName.visibility = View.GONE
            binding.playerAlbumNameData.visibility = View.GONE
        } else {
            val maxSymbols = 30

            if (currentContent.collectionName.length > maxSymbols) {
                binding.playerAlbumNameData.text =
                    currentContent.collectionName.substring(0, maxSymbols) + "..."
            } else {
                binding.playerAlbumNameData.text = currentContent.collectionName
            }

        }
        binding.yearOfReliseData.text = currentContent.releaseDate.substring(0, 4)
        binding.genreData.text = currentContent.primaryGenreName
        binding.countryData.text = currentContent.country

        binding.menuButton.setOnClickListener {
            viewModel.stopPlayer()
            findNavController().navigateUp()
        }

        binding.likeButton.setOnClickListener {
            //Log.i("Click", "Лайк")
            viewModel.likeClickInteractor(track = currentContent)

        }
        val bottomSheetContainer = binding.bottomSheet

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.playlistimageView.setOnClickListener {
            viewModel.getPlaylistList()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
}