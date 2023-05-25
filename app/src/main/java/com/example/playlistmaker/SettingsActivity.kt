package com.example.playlistmaker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class SettingsActivity : Activity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_screen)
        val back = findViewById<ImageButton>(R.id.backToMainActivity)
        back.setOnClickListener {
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backToMainActivity -> {
                finish()
            }
        }
    }
}
