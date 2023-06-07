package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val editText = findViewById<EditText>(R.id.editText)
        val imageViewButton = findViewById<ImageView>(R.id.cancelInputSearchEditText)
        val inputMethod = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Ничего не делает
            }

            override fun afterTextChanged(s: Editable?) {
                //Ничего не делает
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotEmpty() == true) {
                    imageViewButton.visibility = View.VISIBLE
                } else {
                    imageViewButton.visibility = View.GONE
                }
            }
        })
        imageViewButton.setOnClickListener {
            editText.setText("")
            inputMethod.hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }
}
