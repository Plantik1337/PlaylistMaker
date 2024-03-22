package com.example.playlistmaker.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FavoriteFragmentBinding

class FavoriteFragment : Fragment() {
    companion object {
        private const val FAVORITE_FRAGMENT = "FAVORITE_FRAGMENT"

        fun newInstance(number: Int) = FavoriteFragment().apply {
            arguments = Bundle().apply {
                putInt(FAVORITE_FRAGMENT, number)
            }
        }
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
        //
    }
}