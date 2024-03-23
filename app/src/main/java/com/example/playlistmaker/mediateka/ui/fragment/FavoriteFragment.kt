package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FavoriteFragmentBinding
import com.example.playlistmaker.mediateka.viewmodel.FavoriteViewMoel
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

    private val viewModel: FavoriteViewMoel by viewModel {
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
        //
    }
}