package com.example.playlistmaker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class SettingsActivity : Activity() {

    private val shareButton = findViewById<MaterialButton>(R.id.shareButton)
    private val backButton = findViewById<MaterialButton>(R.id.backToMainActivity)
    private val contactUsButton = findViewById<MaterialButton>(R.id.contactUs)
    private val termsOfUseButton = findViewById<MaterialButton>(R.id.termsOfUse)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_screen)

        backButton.setOnClickListener {// Выход с экрана настроек
            finish()
        }


        shareButton.setOnClickListener {//Поделиться
            val courseLink = resources.getString(R.string.practicum)
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, courseLink)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, null))
        }
        contactUsButton.setOnClickListener {// Связь с техподдержкой
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // тип данных для отправки
                putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.MyEmail)))// почта
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    resources.getString(R.string.MessageSbjectViaEmail) // Тема сообщения
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    resources.getString(R.string.MessageViaEmail) // Текст сообщения
                )
                startActivity(this)
            }
        }
        termsOfUseButton.setOnClickListener {// Пользовательское соглашение
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse(resources.getString(R.string.OfferLink)) // Ссылка на пользовательское соглашение
            startActivity(intent)
        }
    }
}
