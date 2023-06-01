package com.example.playlistmaker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.button.MaterialButton


class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_screen)

        findViewById<MaterialButton>(R.id.backToMainActivity).setOnClickListener {// Выход с экрана настроек
            finish()
        }

        findViewById<MaterialButton>(R.id.shareButton).setOnClickListener {//Поделиться
            val courseLink = "https://praktikum.yandex.ru/android-developer/"
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, courseLink)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, null))
        }
        findViewById<MaterialButton>(R.id.contactUs).setOnClickListener {// Связь с техподдержкой
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // тип данных для отправки
                putExtra(Intent.EXTRA_EMAIL, arrayOf("Thesol590@yandex.ru"))// почта
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    "Сообщение разработчикам и разработчицам приложения Playlist Maker" // Тема сообщения
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Спасибо разработчикам и разработчицам за крутое приложение!" // Текст сообщения
                )
            }
            startActivity(intent)
        }
        findViewById<MaterialButton>(R.id.termsOfUse).setOnClickListener {// Пользовательское соглашение
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://yandex.ru/legal/practicum_offer/") // Ссылка на пользовательское соглашение
            startActivity(intent)
        }
    }
}
