package com.example.playlistmaker.mediateka.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.mediateka.ui.fragment.FavoriteFragment
import com.example.playlistmaker.mediateka.ui.fragment.PlaylistFragment

class MediatekaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FavoriteFragment.newInstance()
            }

            else -> {
                PlaylistFragment.newInstance()
            }
        }
    }
}