package com.example.playlistmaker.mediateka.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatekaBinding
import com.example.playlistmaker.mediateka.ui.MediatekaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class MediatekaFragment : Fragment() {

    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var binding: FragmentMediatekaBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediatekaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediatekaViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabElementView = TextView(context)
            tabElementView.textSize = 16f
            tabElementView.gravity = TextView.TEXT_ALIGNMENT_GRAVITY

            tabElementView.text = when (position) {
                0 -> {
                    resources.getString(R.string.FavoriteTracks)
                }

                1 -> {
                    resources.getString(R.string.Playlists)
                }

                else -> ""
            }

            tab.view.background =
                ContextCompat.getDrawable(requireContext(), android.R.color.transparent)


            tabElementView.background =
                ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
            tabElementView.setTextAppearance(R.style.tabTextAppearance)
            tab.customView = tabElementView

        }
        tabMediator.attach()


    }

    override fun onDestroy() {
        super.onDestroy()
    }
}