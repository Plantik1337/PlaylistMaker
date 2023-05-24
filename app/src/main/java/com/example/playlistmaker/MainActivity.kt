package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener(this@MainActivity)
        val mediatekaButton = findViewById<Button>(R.id.mediateka_button)
        mediatekaButton.setOnClickListener(this@MainActivity)
        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener(this@MainActivity)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_button -> {
                v?.setOnClickListener {
                    val displayButton = Intent(this, SearchActivity::class.java)
                    startActivity(displayButton)
                }
            }

            R.id.mediateka_button -> {
                v?.setOnClickListener {
                    val displayButton = Intent(this, MediatekaActivity::class.java)
                    startActivity(displayButton)
                }
            }

            R.id.settings_button -> {
                v?.setOnClickListener {
                    val displayIntent = Intent(this, SettingsActivity::class.java)
                    startActivity(displayIntent)
                }
            }
        }
    }
}

