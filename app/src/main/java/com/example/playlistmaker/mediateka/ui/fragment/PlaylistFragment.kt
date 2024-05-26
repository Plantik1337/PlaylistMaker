package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.mediateka.ui.PlaylistAdapter
import com.example.playlistmaker.mediateka.ui.RecyclerViewClickListener
import com.example.playlistmaker.mediateka.ui.viewmodel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {
    companion object {
        private const val PLAYLIST_FRAGMENT = "PLAYLIST_FRAGMENT"

        fun newInstance() = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putString(PLAYLIST_FRAGMENT, PLAYLIST_FRAGMENT)
            }
        }
    }

    private val viewModel by viewModel<PlaylistViewModel> {
        parametersOf("Строка")
    }


    private lateinit var binding: PlaylistFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_createPlaylistFragment)
        }

        val recyclerView = binding.rvPlaylists

        val spanCount: Int = 2

        recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)

        viewModel.playlistLiveData().observe(viewLifecycleOwner) { playlists ->
            if (playlists.isEmpty()) {
                binding.problemImage.isVisible = true
                binding.problemTextView.isVisible = true
                binding.rvPlaylists.isVisible = false
            } else {
                binding.rvPlaylists.isVisible = true
                binding.problemImage.isVisible = false
                binding.problemTextView.isVisible = false

                binding.rvPlaylists.adapter = recyclerViewInteractor(playlists)
            }
        }
        viewModel.getPlaylists()

    }

    private fun recyclerViewInteractor(playlists: List<Playlist>): PlaylistAdapter {
        val adapter = PlaylistAdapter(playlists, object : RecyclerViewClickListener {
            override fun onItemClick(position: Int) {
                findNavController().navigate(
                    R.id.action_mediatekaFragment_to_inspectPlaylistFragment,
                    InspectPlaylistFragment.createArgs(playlists[position])
                )
            }
        })
        return adapter
    }
}