package com.example.playlistmaker.settings.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.SettingsScreenBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModel<SettingsViewModel>()

        //private val context: Context by inject { parametersOf(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = SettingsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeSwitcher.isChecked = viewModel.isDarkMode()

        binding.backToMainActivity.setOnClickListener {// Выход с экрана настроек
            finish()
        }

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

//        viewModel.getLiveData().observe(this){intent ->
//            when(intent){
//
//            }
//        }

    }
}
