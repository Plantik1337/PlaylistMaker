package com.example.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.Statement
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var editText: EditText
    private var savedTextSearch: String? = ""
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val viewModel: SearchViewModel by viewModel<SearchViewModel>()

    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivitySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editText = binding.searchEditText

        val inputMethod =
            requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        val emptyTrackList = emptyList<Track>()

        fun recyclerViewInteractor(track: List<Track>): Adapter {
            val adapter = Adapter(track, object : RecyclerViewClickListener {
                override fun onItemClick(position: Int) {
                    if (clickDebounce()) {
                        viewModel.writeToHistory(track[position])
                        findNavController().navigate(
                            R.id.action_searchFragment_to_playerActivity,
                            PlayerFragment.createArgs(
                                trackName = track[position].trackName,
                                artistName = track[position].artistName,
                                trackTimeMillis = track[position].trackTimeMillis,
                                artworkUrl100 = track[position].artworkUrl100,
                                previewUrl = track[position].previewUrl,
                                releaseDate = track[position].releaseDate,
                                country = track[position].country,
                                primaryGenreName = track[position].primaryGenreName,
                                collectionName = track[position].collectionName,
                                collectionExplicitness = track[position].collectionExplicitness
                            )
                        )
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

        viewModel.getTracklistLiveData().observe(viewLifecycleOwner) { screenState ->
            //Log.i("Состояние", screenState.toString())
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
                    //binding.statusText
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
        }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        editText.addTextChangedListener(onTextChanged = { s: CharSequence?, _: Int, _: Int, _: Int ->

            when (editText.hasFocus() && s?.isEmpty() == true) {
                true -> {
                    binding.historyTextView.visibility = View.VISIBLE
                    binding.clearHistoryButton.visibility = View.VISIBLE
                }

                false -> {
                    binding.historyTextView.visibility = View.GONE
                    binding.clearHistoryButton.visibility = View.GONE
                    binding.trackListRecyclerView.adapter = recyclerViewInteractor(emptyTrackList)
                }
            }
            if (s?.isNotEmpty() == true) {
                binding.cancelInputSearchEditText.visibility = View.VISIBLE
                searchDebounce()

            } else {
                binding.cancelInputSearchEditText.visibility = View.GONE
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

        binding.refrashButton.setOnClickListener {// Повторный запрос при нажатии на кнопку "Refresh"
            binding.searchProblems.visibility = View.GONE
            binding.internetProblemText.visibility = View.GONE
            binding.refrashButton.visibility = View.GONE
            binding.historyTextView.visibility = View.GONE
            viewModel.doRequest(editText.text.toString())
        }

        binding.cancelInputSearchEditText.setOnClickListener {
            editText.setText("")
            binding.searchProblems.visibility = View.GONE
            inputMethod.hideSoftInputFromWindow(editText.windowToken, 0)
            viewModel.history()
        }
        if (savedInstanceState != null) {
            savedTextSearch = savedInstanceState.getString(EDIT_TEXT, "")
            editText.setText(savedTextSearch)
        }
        binding.trackListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    override fun onDetach() {
        super.onDetach()
    }

}

