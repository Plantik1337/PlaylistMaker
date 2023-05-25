package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity(), View.OnClickListener {
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_button -> {
                val displayButton = Intent(this, SearchActivity::class.java)
                startActivity(displayButton)
            }

            R.id.mediateka_button -> {
                val displayButton = Intent(this, MediatekaActivity::class.java)
                startActivity(displayButton)

            }

            R.id.settings_button -> {
                val displayButton = Intent(this, SettingsActivity::class.java)
                startActivity(displayButton)
            }
        }
    }
}

