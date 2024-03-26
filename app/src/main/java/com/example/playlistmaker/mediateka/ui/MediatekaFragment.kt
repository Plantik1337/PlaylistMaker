package com.example.playlistmaker.mediateka.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaFragment : Fragment() {

    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var binding: ActivityMediatekaBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMediatekaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val binding = ActivityMediatekaBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        binding.viewPager.adapter = MediatekaViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.FavoriteTracks)
                1 -> tab.text = resources.getString(R.string.Playlists)
            }
        }
        tabMediator.attach()

//        binding.backToMainActivity.setOnClickListener {
//            finish()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}