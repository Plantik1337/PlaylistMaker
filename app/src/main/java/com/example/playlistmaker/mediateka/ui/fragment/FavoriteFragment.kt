package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FavoriteFragmentBinding
import com.example.playlistmaker.mediateka.ui.MediatekaAdapter
import com.example.playlistmaker.mediateka.ui.RecyclerViewClickListener
import com.example.playlistmaker.mediateka.viewmodel.FavoriteViewModel
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.search.ui.SearchFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteFragment : Fragment() {
    companion object {
        private const val FAVORITE_FRAGMENT = "FAVORITE_FRAGMENT"

        fun newInstance() = FavoriteFragment().apply {
            arguments = Bundle().apply {
                putString(FAVORITE_FRAGMENT, FAVORITE_FRAGMENT)
            }
        }
    }

    private val viewModel: FavoriteViewModel by viewModel {
        parametersOf("Строка")
    }

    private lateinit var binding: FavoriteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showLog()


        fun recyclerViewInteractor(tracks: List<Track>): MediatekaAdapter{
            val adapter = MediatekaAdapter(tracks, object : RecyclerViewClickListener{
                override fun onItemClick(position: Int) {
                    binding.favoriteFragmentRV. isEnabled = false

                    Log.i("Click!", tracks[position].trackName)

                    findNavController().navigate(
                        R.id.action_favoriteFragment_to_playerActivity,
                        PlayerFragment.createArgs(
                            trackId = tracks[position].trackId,
                            trackName = tracks[position].trackName,
                            artistName = tracks[position].artistName,
                            trackTimeMillis = tracks[position].trackTimeMillis,
                            artworkUrl100 = tracks[position].artworkUrl100,
                            previewUrl = tracks[position].previewUrl,
                            releaseDate = tracks[position].releaseDate,
                            country = tracks[position].country,
                            primaryGenreName = tracks[position].primaryGenreName,
                            collectionName = tracks[position].collectionName,
                            collectionExplicitness = tracks[position].collectionExplicitness
                        )
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(400L)
                        binding.favoriteFragmentRV.isEnabled = true
                        Log.i("Статус нажатия", "Можешь нажимать")
                    }
                }
            })
            return adapter
        }
        //binding.favoriteFragmentRV.adapter
        //
    }


}