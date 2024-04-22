package com.example.playlistmaker.player.ui

import android.graphics.Outline
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {

    private lateinit var binding: ActivityPlayerBinding

    companion object {

        const val TRACK_NAME = "TRACK_NAME"
        const val ARTIST_NAME = "ARTIST_NAME"
        const val TRACK_TIME_MILLIS = "TRACK_TIME_MILLIS"
        const val ARTWORK_URL100 = "ARTWORK_URL100"
        const val PREVIEW_URL = "PREVIEW_URL"
        const val RELEASE_DATE = "RELEASE_DATE"
        const val COUNTRY = "COUNTRY"
        const val PRIMARY_GENRENAME = "PRIMARY_GENRENAME"
        const val COLLECTION_NAME = "COLLECTION_NAME"
        const val COLLECTION_EXPLICITNESS = "COLLECTION_EXPLICITNESS"

        fun createArgs(
            trackName: String,
            artistName: String,
            trackTimeMillis: String,
            artworkUrl100: String,
            previewUrl: String,
            releaseDate: String,
            country: String,
            primaryGenreName: String,
            collectionName: String,
            collectionExplicitness: String
        ): Bundle = bundleOf(
            TRACK_NAME to trackName,
            ARTIST_NAME to artistName,
            TRACK_TIME_MILLIS to trackTimeMillis,
            ARTWORK_URL100 to artworkUrl100,
            PREVIEW_URL to previewUrl,
            RELEASE_DATE to releaseDate,
            COUNTRY to country,
            PRIMARY_GENRENAME to primaryGenreName,
            COLLECTION_NAME to collectionName,
            COLLECTION_EXPLICITNESS to collectionExplicitness
        )
    }

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(requireArguments().getString(PREVIEW_URL))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentContent = Track(
            trackName = requireArguments().getString(TRACK_NAME).toString(),
            artistName = requireArguments().getString(ARTIST_NAME).toString(),
            trackTimeMillis = requireArguments().getString(TRACK_TIME_MILLIS).toString(),
            artworkUrl100 = requireArguments().getString(ARTWORK_URL100).toString(),
            previewUrl = requireArguments().getString(PREVIEW_URL).toString(),
            releaseDate = requireArguments().getString(RELEASE_DATE).toString(),
            country = requireArguments().getString(COUNTRY).toString(),
            primaryGenreName = requireArguments().getString(PRIMARY_GENRENAME).toString(),
            collectionName = requireArguments().getString(COLLECTION_NAME).toString(),
            collectionExplicitness = requireArguments().getString(COLLECTION_EXPLICITNESS)
                .toString()
        )

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

    }

    override fun onDetach() {
        super.onDetach()
    }
}