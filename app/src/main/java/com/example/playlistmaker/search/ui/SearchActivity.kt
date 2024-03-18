package com.example.playlistmaker.search.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.Statement
import com.example.playlistmaker.search.domain.Adapter
import com.example.playlistmaker.search.domain.RecyclerViewClickListener
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private var savedTextSearch: String? = ""
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var viewModel: SearchViewModel


    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"

        // const val TAG = "MUSIC_STATE"
        const val HISTORY_LIST = "HISTORY_LIST"

        //        private const val TRACK = "TRACK"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editText = binding.searchEditText

        //val sharedPreferences: SharedPreferences = getSharedPreferences(HISTORY_LIST, MODE_PRIVATE)

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory(
                getSharedPreferences(HISTORY_LIST, MODE_PRIVATE),
                this
            )
        )[SearchViewModel::class.java]

        //val historyTransaction: HistoryRepository = HistoryTransaction()
        val inputMethod = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val emptyTrackList = emptyList<Track>()

        binding.backToMainActivityFromSearchActivity.setOnClickListener {
            finish()
        }

        fun recyclerViewInteractor(track: List<Track>): Adapter {
            val adapter = Adapter(track,
                object : RecyclerViewClickListener {
                    override fun onItemClick(position: Int) {

                        if (clickDebounce()) {
                            viewModel.writeToHistory(track[position])
                            val playerActivity =
                                Intent(this@SearchActivity, PlayerActivity::class.java)
                            playerActivity.putExtra("trackName", track[position].trackName)
                            playerActivity.putExtra("artistName", track[position].artistName)
                            playerActivity.putExtra(
                                "trackTimeMillis",
                                track[position].trackTimeMillis
                            )
                            playerActivity.putExtra("artworkUrl100", track[position].artworkUrl100)
                            playerActivity.putExtra("previewUrl", track[position].previewUrl)
                            playerActivity.putExtra("releaseDate", track[position].releaseDate)
                            playerActivity.putExtra("country", track[position].country)
                            playerActivity.putExtra(
                                "primaryGenreName",
                                track[position].primaryGenreName
                            )
                            playerActivity.putExtra(
                                "collectionName",
                                track[position].collectionName
                            )
                            playerActivity.putExtra(
                                "collectionExplicitness",
                                track[position].collectionExplicitness
                            )
                            Log.i("Track", track[position].toString())
                            startActivity(playerActivity)
                        }
                    }
                })
            return adapter
        }


        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            binding.clearHistoryButton.visibility = View.GONE
            binding.historyTextView.visibility = View.GONE
            binding.trackListRecyclerView.adapter = recyclerViewInteractor(emptyTrackList)
            viewModel.history()
        }

        viewModel.getTracklistLiveData().observe(this) { screenState ->
            Log.i("Состояние", screenState.toString())
            when (screenState) {

                is Statement.HISTORY -> {
                    binding.searchProgressBar.visibility = View.GONE
                    binding.trackListRecyclerView.visibility = View.VISIBLE
                    binding.trackListRecyclerView.adapter =
                        recyclerViewInteractor(screenState.trackList)
                    if (screenState.trackList.isEmpty()) {
                        binding.historyTextView.visibility = View.GONE
                        binding.clearHistoryButton.visibility = View.GONE

                    } else {
                        binding.historyTextView.visibility = View.VISIBLE
                        binding.clearHistoryButton.visibility = View.VISIBLE
                    }
                }

                is Statement.Error -> {
                    binding.searchProgressBar.visibility = View.GONE
                    when (screenState.errorMessage) {
                        "-1" -> {
                            binding.searchProblems.visibility = View.VISIBLE
                            binding.statusText.text = getString(R.string.internetProblem)
                            binding.internetProblemText.visibility = View.VISIBLE
                            binding.problemImage.setImageResource(R.drawable.internet_problems_vector)
                            binding.refrashButton.visibility = View.VISIBLE
                            binding.historyTextView.visibility = View.GONE
                            binding.trackListRecyclerView.visibility = View.GONE
                        }

                        "empty" -> {
                            binding.searchProblems.visibility = View.VISIBLE
                            binding.statusText.text = getString(R.string.searchNothing)
                            binding.problemImage.setImageResource(R.drawable.search_problems_vector)
                            binding.historyTextView.visibility = View.GONE
                            binding.trackListRecyclerView.visibility = View.GONE
                        }
                    }
                }

                is Statement.Success -> {
                    binding.searchProgressBar.visibility = View.GONE
                    Log.i("успешный запрос", screenState.trackList.toString())
                    binding.trackListRecyclerView.visibility = View.VISIBLE
                    binding.trackListRecyclerView.adapter =
                        recyclerViewInteractor(screenState.trackList)
                }

                Statement.Loading -> {
                    binding.searchProgressBar.visibility = View.VISIBLE
                }
            }

        }

        val searchRunnable = Runnable {
            viewModel.doRequest(editText.text.toString())
            //binding.searchProgressBar.visibility = View.VISIBLE
        }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            //binding.searchProgressBar.visibility = View.GONE
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)

            //viewModel.doRequest(editText.text.toString())
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
                        binding.historyTextView.visibility = View.VISIBLE
                        binding.clearHistoryButton.visibility = View.VISIBLE
                        //showHistory()
                    }

                    false -> {
                        binding.historyTextView.visibility = View.GONE
                        binding.clearHistoryButton.visibility = View.GONE
                        binding.trackListRecyclerView.adapter =
                            recyclerViewInteractor(emptyTrackList)
                    }
                }
                if (s?.isNotEmpty() == true) {
                    binding.cancelInputSearchEditText.visibility = View.VISIBLE
                    searchDebounce()

                } else {
                    binding.cancelInputSearchEditText.visibility = View.GONE
                }
            }
        })
        editText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    viewModel.doRequest(editText.text.toString())
                }
            }
            false
        }


        binding.refrashButton.setOnClickListener {
            // Повторный запрос при нажатии на кнопку "Refresh"
            binding.searchProblems.visibility = View.GONE
            binding.internetProblemText.visibility = View.GONE
            binding.refrashButton.visibility = View.GONE
            binding.historyTextView.visibility = View.GONE
            viewModel.doRequest(editText.text.toString())
        }

        binding.cancelInputSearchEditText.setOnClickListener {
            editText.setText("")
            binding.searchProblems.visibility = View.GONE
            //showHistory()
            inputMethod.hideSoftInputFromWindow(editText.windowToken, 0)
            viewModel.history()
        }
        if (savedInstanceState != null) {
            savedTextSearch = savedInstanceState.getString(EDIT_TEXT, "")
            editText.setText(savedTextSearch)
        }
        binding.trackListRecyclerView.layoutManager = LinearLayoutManager(this)
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

