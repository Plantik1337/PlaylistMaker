package com.example.playlistmaker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class SettingsActivity : Activity(),View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_screen)
        val back = findViewById<ImageButton>(R.id.backToMainActivity)
        back.setOnClickListener(this@SettingsActivity)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.backToMainActivity ->{
                v?.setOnClickListener {
                    val displayButton = Intent(this, MainActivity::class.java)
                    startActivity(displayButton)
                }
            }
        }
    }
}