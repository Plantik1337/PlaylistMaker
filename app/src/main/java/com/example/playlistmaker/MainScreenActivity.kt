package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainScreenActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)
        val image = findViewById<ImageView>(R.id.poster)// Ссылка на постер
        image.setOnClickListener(this@MainScreenActivity)
        image.setBackgroundColor(getColor(R.color.blue))
        image.scaleType = ImageView.ScaleType.CENTER_CROP

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.poster -> {
                Toast.makeText(this, "Нажали на картинку!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}