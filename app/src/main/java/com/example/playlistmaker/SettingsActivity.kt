package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButton

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_screen)

        val shareButton = findViewById<MaterialButton>(R.id.shareButton)
        val backButton = findViewById<MaterialButton>(R.id.backToMainActivity)
        val contactUsButton = findViewById<MaterialButton>(R.id.contactUs)
        val termsOfUseButton = findViewById<MaterialButton>(R.id.termsOfUse)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.themeSwitcher)

        backButton.setOnClickListener {// Выход с экрана настроек
            finish()
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
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
