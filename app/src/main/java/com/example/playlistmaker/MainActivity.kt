package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val image = findViewById<ImageView>(R.id.poster)// Ссылка на постер
        image.setOnClickListener(this@MainActivity)
        image.setBackgroundColor(getColor(R.color.blue))
        image.scaleType = ImageView.ScaleType.CENTER_CROP

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.poster -> {
                Toast.makeText(this, "Нажали на картинку!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class MainScreenActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.main_screen)
        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener(this@MainScreenActivity)
        val mediatekaButton = findViewById<Button>(R.id.mediateka_button)
        mediatekaButton.setOnClickListener(this@MainScreenActivity)
        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener(this@MainScreenActivity)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_button -> {
                Toast.makeText(this, "Поиск", Toast.LENGTH_SHORT).show()
            }

            R.id.mediateka_button -> {
                Toast.makeText(this, "Медиатека", Toast.LENGTH_SHORT).show()
            }

            R.id.settings_button -> {
                Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
            }
        }
    }
}