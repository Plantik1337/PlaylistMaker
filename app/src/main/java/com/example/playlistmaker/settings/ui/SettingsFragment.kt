package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.SettingsScreenBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel<SettingsViewModel>()

    private lateinit var binding: SettingsScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.themeSwitcher.isChecked = viewModel.isDarkMode() //TODO исправить

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            Log.i("switching theme", "${checked}")
            viewModel.themeSwitch(checked)
        }

        binding.shareButton.setOnClickListener {//Поделиться
            viewModel.share()
        }
        binding.contactUs.setOnClickListener {// Связь с техподдержкой
            viewModel.supportContact()
        }

        binding.termsOfUse.setOnClickListener {// Пользовательское соглашение
            viewModel.termOfUse()
        }

    }
}
