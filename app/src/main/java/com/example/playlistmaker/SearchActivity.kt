package com.example.playlistmaker

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SearchActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private var savedTextSearch: String? = ""

    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        lateinit var adapter: Adapter

        val editText = findViewById<EditText>(R.id.searchEditText)
        val imageViewButton = findViewById<ImageView>(R.id.cancelInputSearchEditText)
        val inputMethod = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val trackListView = findViewById<RecyclerView>(R.id.trackListRecyclerView)
        val trackList: ArrayList<Track> = arrayListOf(
            Track(
                trackName = "Smells Like Teen Spirit (REMASTERED 2011)",//дописал для проверки вывода
                artistName = "Nirvana",
                trackTime = "5:01",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                trackName = "Billie Jean",
                artistName = "Michael Jackson",
                trackTime = "4:35",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),
            Track(
                trackName = "Stayin' Alive",
                artistName = "Bee Gees",
                trackTime = "4:10",
                artworkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                trackName = "Whole Lotta Love",
                artistName = "Led Zeppelin",
                trackTime = "5:33",
                artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),
            Track(
                trackName = "Sweet Child O'Mine",
                artistName = "Guns N' Roses",
                trackTime = "5:03",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            )
        )
        findViewById<MaterialButton>(R.id.backToMainActivityFromSearchActivity).setOnClickListener {
            finish()
        }

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
        if (savedInstanceState != null) {
            savedTextSearch = savedInstanceState.getString(EDIT_TEXT, "")
            editText.setText(savedTextSearch)
        }
        trackListView.layoutManager = LinearLayoutManager(this)

        adapter = Adapter(trackList)
        trackListView.adapter = adapter

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        savedTextSearch = editText.text.toString()
        outState.putString(EDIT_TEXT, savedTextSearch)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?, persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        savedTextSearch = savedInstanceState?.getString(EDIT_TEXT, "")
        editText.setText(savedTextSearch)
    }



}


