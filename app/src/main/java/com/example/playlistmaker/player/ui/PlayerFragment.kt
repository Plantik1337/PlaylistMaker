package com.example.playlistmaker.player.ui

import android.graphics.Outline
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
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.databinding.FragmentPlayerBinding
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
        val track = requireArguments().getParcelable(TRACK_KEY, Track::class.java)!!
        parametersOf(track.trackId, track.previewUrl)
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

        val currentContent = requireArguments().getParcelable(TRACK_KEY, Track::class.java)!!


        viewModel.isTrackLiked()

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.isExists(currentContent.trackId)
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
    }

    override fun onDetach() {
        super.onDetach()
    }
}