package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class SearchActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private var savedTextSearch: String? = ""

    //val gson = Gson()

    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        const val TAG = "MUSIC_STATE"
    }


    @SuppressLint("LocalSuppress")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        lateinit var adapter: Adapter

        val editText = findViewById<EditText>(R.id.searchEditText)
        val imageViewButton = findViewById<ImageView>(R.id.cancelInputSearchEditText)
        val inputMethod = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val trackListView = findViewById<RecyclerView>(R.id.trackListRecyclerView)

        val problemsSearchEvent = findViewById<LinearLayout>(R.id.searchProblems)
        val problemsInternetSearchEvent = findViewById<LinearLayout>(R.id.searchInternetProblems)


        val baseUrl = "https://itunes.apple.com"

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

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

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                problemsInternetSearchEvent.visibility = View.GONE
                problemsSearchEvent.visibility = View.GONE

                val appleMusicServer = retrofit.create(AppleMusicServer::class.java)

                appleMusicServer.search(editText.text.toString())
                    .enqueue(object : Callback<MusicResponse> {
                        override fun onResponse(
                            call: Call<MusicResponse>,
                            response: Response<MusicResponse>
                        ) {
                            if (response.body()?.resultCount ?: 0 != "0") {
                                Log.d(TAG, "onResponse: ${response.body()?.results}")

                                when (response.code()) {
                                    200 -> { // песни найдены
                                        val musicCollection = response.body()!!.results
                                        adapter = Adapter(musicCollection)
                                        trackListView.adapter = adapter
                                    }
                                }
                            } else {//песни не найдены
                                Log.e(TAG, "Что-то пошло не так, серер не отдаёт список песен")
                                val emptyListTrack:ArrayList<Track> = ArrayList()
                                trackListView.adapter = Adapter(emptyListTrack)
                                problemsSearchEvent.visibility = View.VISIBLE
                            }


                        }

                        override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                            Log.d(TAG, "onFailure: $t")
                            // TODO("Вывести сообщение об ошибке подключения")
                            val emptyListTrack:ArrayList<Track> = ArrayList()
                            trackListView.adapter = Adapter(emptyListTrack)
                            problemsInternetSearchEvent.visibility = View.VISIBLE
                        }
                    })
                true
            }
            false
        }

        val refrashButton = findViewById<MaterialButton>(R.id.refrashButton)

        refrashButton.setOnClickListener {
            // Повторный запрос при нажатии на кнопку "Refresh"
            val appleMusicServer = retrofit.create(AppleMusicServer::class.java)

            appleMusicServer.search(editText.text.toString())
                .enqueue(object : Callback<MusicResponse> {
                    override fun onResponse(call: Call<MusicResponse>, response: Response<MusicResponse>) {
                        if (response.body()?.resultCount ?: 0 != "0") {
                            Log.d(TAG, "onResponse: ${response.body()?.results}")

                            when (response.code()) {
                                200 -> { // песни найдены
                                    problemsSearchEvent.visibility = View.GONE
                                    problemsInternetSearchEvent.visibility = View.GONE
                                    val musicCollection = response.body()!!.results
                                    adapter = Adapter(musicCollection)
                                    trackListView.adapter = adapter
                                }
                            }
                        } else {//песни не найдены
                            Log.e(TAG, "Что-то пошло не так, сервер не отдаёт список песен")
                            val emptyListTrack:ArrayList<Track> = ArrayList()
                            trackListView.adapter = Adapter(emptyListTrack)
                            problemsInternetSearchEvent.visibility = View.GONE
                            problemsSearchEvent.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                        Log.d(TAG, "onFailure: $t")
                        // Вывести сообщение об ошибке подключения
                        val emptyListTrack:ArrayList<Track> = ArrayList()
                        trackListView.adapter = Adapter(emptyListTrack)
                        problemsSearchEvent.visibility = View.GONE
                        problemsInternetSearchEvent.visibility = View.VISIBLE
                    }
                })
        }

        imageViewButton.setOnClickListener {
            editText.setText("")
            val emptyListTrack:ArrayList<Track> = ArrayList()
            trackListView.adapter = Adapter(emptyListTrack)
            inputMethod.hideSoftInputFromWindow(editText.windowToken, 0)
        }
        if (savedInstanceState != null) {
            savedTextSearch = savedInstanceState.getString(EDIT_TEXT, "")
            editText.setText(savedTextSearch)
        }
        trackListView.layoutManager = LinearLayoutManager(this)

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

interface AppleMusicServer {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<MusicResponse>
}

