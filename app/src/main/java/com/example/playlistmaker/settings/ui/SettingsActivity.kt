package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.SettingsScreenBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModelFactory

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = SettingsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingsViewModelFactory(this))[SettingsViewModel::class.java]


        binding.backToMainActivity.setOnClickListener {// Выход с экрана настроек
            finish()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
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
