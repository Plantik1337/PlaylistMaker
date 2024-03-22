package com.example.playlistmaker.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.PlaylistFragmentBinding

class PlaylistFragment : Fragment() {
    companion object {
        private const val PLAYLIST_FRAGMENT = "PLAYLIST_FRAGMENT"

        fun newInstance(number: Int) = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putInt(PLAYLIST_FRAGMENT, number)
            }
        }
    }

    private lateinit var binding: PlaylistFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
    }
}