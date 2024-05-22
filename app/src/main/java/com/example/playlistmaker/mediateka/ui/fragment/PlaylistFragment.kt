package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.mediateka.viewmodel.PlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        viewModel.showLog()
        //
    }
}