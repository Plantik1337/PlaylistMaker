package com.example.playlistmaker.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.SettingsViewModel
import com.google.android.material.button.MaterialButton

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_screen)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(
                resources.getString(R.string.practicum),
                resources.getString(R.string.MyEmail),
                resources.getString(R.string.MessageSbjectViaEmail),
                resources.getString(R.string.MessageViaEmail),
                resources.getString(R.string.Terms_of_use)
            )
        )[SettingsViewModel::class.java]

        val shareButton = findViewById<MaterialButton>(R.id.shareButton)
        val backButton = findViewById<MaterialButton>(R.id.backToMainActivity)
        val contactUsButton = findViewById<MaterialButton>(R.id.contactUs)
        val termsOfUseButton = findViewById<MaterialButton>(R.id.termsOfUse)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.themeSwitcher)

        viewModel.intentLiveData.observe(this) {
            startActivity(Intent.createChooser(it, null))
        }

        backButton.setOnClickListener {// Выход с экрана настроек
            finish()
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        shareButton.setOnClickListener {//Поделиться
            viewModel.onShareClick()
        }
        contactUsButton.setOnClickListener {// Связь с техподдержкой
            viewModel.onContactUsClick()
        }

        termsOfUseButton.setOnClickListener {// Пользовательское соглашение
            viewModel.onTermsOfUseClick()
        }
    }
}
