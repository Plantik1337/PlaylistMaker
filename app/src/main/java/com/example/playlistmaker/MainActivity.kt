package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<MaterialButton>(R.id.search_button)
        searchButton.setOnClickListener {
            val displayButton = Intent(this, SearchActivity::class.java)
            startActivity(displayButton)
        }
        val mediatekaButton = findViewById<MaterialButton>(R.id.mediateka_button)
        mediatekaButton.setOnClickListener {
            val displayButton = Intent(this, MediatekaActivity::class.java)
            startActivity(displayButton)
        }
        val settingsButton = findViewById<MaterialButton>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val displayButton = Intent(this, SettingsActivity::class.java)
            startActivity(displayButton)
        }

    }
}

