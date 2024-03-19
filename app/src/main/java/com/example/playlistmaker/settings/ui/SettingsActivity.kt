package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.SettingsScreenBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModel<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = SettingsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeSwitcher.isChecked = viewModel.isDarkMode(this)

        binding.backToMainActivity.setOnClickListener {// Выход с экрана настроек
            finish()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            Log.i("switching theme", "${checked}")
            viewModel.themeSwitch(checked, this)
        }

        binding.shareButton.setOnClickListener {//Поделиться
            viewModel.share(this)
        }
        binding.contactUs.setOnClickListener {// Связь с техподдержкой
            viewModel.supportContact(this)
        }

        binding.termsOfUse.setOnClickListener {// Пользовательское соглашение
            viewModel.termOfUse(this)
        }
    }
}
