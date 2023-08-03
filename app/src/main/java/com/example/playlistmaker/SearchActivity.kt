package com.example.playlistmaker

import android.content.SharedPreferences
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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private var savedTextSearch: String? = ""


    //val gson = Gson()

    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        const val TAG = "MUSIC_STATE"
        const val HISTORY_LIST = "HISTORY_LIST"
    }

    private enum class Status {
        ERROR_INTERNET,
        ERROR_NOT_FOUND,
        SUCCESS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_search)

        val sharedPreferences: SharedPreferences = getSharedPreferences(HISTORY_LIST, MODE_PRIVATE)
        //lateinit var adapter: Adapter

        val editText = findViewById<EditText>(R.id.searchEditText)
        val imageViewButton = findViewById<ImageView>(R.id.cancelInputSearchEditText)
        val inputMethod = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val trackListView = findViewById<RecyclerView>(R.id.trackListRecyclerView)


        val problemsSearchEvent = findViewById<LinearLayout>(R.id.searchProblems)
        val problemImageView = findViewById<ImageView>(R.id.problemImage)
        val statusText = findViewById<TextView>(R.id.statusText)
        val internetProblemText = findViewById<TextView>(R.id.internetProblemText)
        val refrashButton = findViewById<MaterialButton>(R.id.refrashButton)

        val historyTextView = findViewById<TextView>(R.id.historyTextView)
        val clearHistoryButton = findViewById<MaterialButton>(R.id.clearHistoryButton)
        val historyTransaction = HistoryTransaction()

        val baseUrl = "https://itunes.apple.com"

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        findViewById<MaterialButton>(R.id.backToMainActivityFromSearchActivity).setOnClickListener {
            finish()
        }


        val emptyTrackList = ArrayList<Track>()
        fun historyAdapter(track: ArrayList<Track>): Adapter {
            val adapter = Adapter(track,
                object : RecyclerViewClickListener {
                    override fun onItemClick(position: Int) {
                        historyTransaction.write(sharedPreferences, track[position])
                        trackListView.adapter =
                            historyAdapter(historyTransaction.read(sharedPreferences))
                        trackListView.adapter?.notifyDataSetChanged()
                    }
                })
            adapter.notifyDataSetChanged()
            return adapter
        }

        fun showHistory() {
            trackListView.adapter = historyAdapter(emptyTrackList)
            trackListView.adapter = historyAdapter(historyTransaction.read(sharedPreferences))

            if (historyAdapter(historyTransaction.read(sharedPreferences)).itemCount == 0) {
                clearHistoryButton.visibility = View.GONE
                historyTextView.visibility = View.GONE
            } else {
                clearHistoryButton.visibility = View.VISIBLE
                historyTextView.visibility = View.VISIBLE
            }
        }
        showHistory()
        clearHistoryButton.setOnClickListener {
            historyTransaction.cleanHistory(sharedPreferences)
            clearHistoryButton.visibility = View.GONE
            historyTextView.visibility = View.GONE
            trackListView.adapter = historyAdapter(emptyTrackList)
            showHistory()
        }
        editText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Ничего не делает
            }

            override fun afterTextChanged(s: Editable?) {
                //Ничего не делает

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                when (editText.hasFocus() && s?.isEmpty() == true) {
                    true -> {
                        historyTextView.visibility = View.VISIBLE
                        clearHistoryButton.visibility = View.VISIBLE
                        showHistory()
                    }

                    false -> {
                        historyTextView.visibility = View.GONE
                        clearHistoryButton.visibility = View.GONE
                        trackListView.adapter = historyAdapter(emptyTrackList)
                    }
                }
                if (s?.isNotEmpty() == true) {
                    imageViewButton.visibility = View.VISIBLE

                } else {
                    imageViewButton.visibility = View.GONE
                }
            }
        })


        fun showStatus(status: Status) {
            when (status) {
                Status.ERROR_INTERNET -> {
                    problemsSearchEvent.visibility = View.VISIBLE
                    statusText.text = getString(R.string.internetProblem)
                    internetProblemText.visibility = View.VISIBLE
                    problemImageView.setImageResource(R.drawable.internet_problems_vector)
                    refrashButton.visibility = View.VISIBLE
                }

                Status.ERROR_NOT_FOUND -> {
                    problemsSearchEvent.visibility = View.VISIBLE
                    statusText.text = getString(R.string.searchNothing)
                    problemImageView.setImageResource(R.drawable.search_problems_vector)
                }

                Status.SUCCESS -> {
                    problemsSearchEvent.visibility = View.GONE
                    internetProblemText.visibility = View.GONE
                    refrashButton.visibility = View.GONE
                }
            }
        }

        fun connectionToDataBase() {
            val appleMusicServer = retrofit.create(AppleMusicServer::class.java)

            appleMusicServer.search(editText.text.toString())
                .enqueue(object : Callback<MusicResponse> {
                    override fun onResponse(
                        call: Call<MusicResponse>,
                        response: Response<MusicResponse>
                    ) {
                        if (response.body()?.results != null) {
                            Log.d(TAG, "onResponse: ${response.body()?.results}")

                            when (response.code()) {
                                200 -> { // песни найдены
                                    val musicCollection = response.body()!!.results
                                    val adapter = Adapter(musicCollection,
                                        object : RecyclerViewClickListener {
                                            override fun onItemClick(position: Int) {
                                                //val item: Track = musicCollection[position]
                                                historyTransaction.write(
                                                    sharedPreferences,
                                                    musicCollection[position]
                                                )
                                            }
                                        })
                                    trackListView.adapter = adapter
                                    showStatus(Status.SUCCESS)
                                }
                            }
                        } else {//песни не найдены
                            Log.e(TAG, "Что-то пошло не так, серер не отдаёт список песен")
                            //adapter().clear()
                            trackListView.adapter = historyAdapter(emptyTrackList)
                            showStatus(Status.ERROR_NOT_FOUND)
                        }
                    }

                    override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                        Log.d(TAG, "onFailure: $t")
                        //adapter().clear()
                        trackListView.adapter = historyAdapter(emptyTrackList)
                        showStatus(Status.ERROR_INTERNET)
                    }
                })
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    connectionToDataBase()
                }
            }
            false
        }


        refrashButton.setOnClickListener {
            // Повторный запрос при нажатии на кнопку "Refresh"
            connectionToDataBase()
        }

        imageViewButton.setOnClickListener {
            editText.setText("")
            problemsSearchEvent.visibility = View.GONE
            showHistory()
            inputMethod.hideSoftInputFromWindow(editText.windowToken, 0)
            if (trackListView.isEmpty()) {
                Log.d("Тег", "Кнопка должна появиться")
                historyTextView.visibility = View.VISIBLE
                clearHistoryButton.visibility = View.VISIBLE
            } else {
                Log.d("Тег", "Кнопка не должна появиться")
                historyTextView.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
            }
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

