package com.example.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.Statement
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var editText: EditText
    private var savedTextSearch: String? = ""

    private var searchJob: Job? = null
    private val viewModel: SearchViewModel by viewModel<SearchViewModel>()

    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
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

                    binding.trackListRecyclerView.isEnabled = false

                    Log.i("Click!", track[position].trackName)

                    viewModel.writeToHistory(track[position])
                    findNavController().navigate(
                        R.id.action_searchFragment_to_playerActivity,
                        PlayerFragment.createArgs(
                            track = track[position]
                        )
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(CLICK_DEBOUNCE_DELAY)
                        binding.trackListRecyclerView.isEnabled = true
                        Log.i("Статус нажатия", "Можешь нажимать")
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
                    if (screenState.trackList.isEmpty()) {
                        viewModel.history()
                    }
                }

                Statement.Loading -> {
                    binding.searchProgressBar.visibility = View.VISIBLE
                }

                else -> {}
            }

        }

        fun searchDebounce() {
            searchJob = lifecycleScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                viewModel.doRequest(editText.text.toString())
            }
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
            if (!s.isNullOrBlank()) {
                binding.cancelInputSearchEditText.visibility = View.VISIBLE
                Log.i("Начался запрос в сеть", "Есть такое")
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
            searchJob?.cancel()
        }
        if (savedInstanceState != null) {
            savedTextSearch = savedInstanceState.getString(EDIT_TEXT, "")
            editText.setText(savedTextSearch)
        }
        binding.trackListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDetach() {
        searchJob?.cancel()
        super.onDetach()
    }

}

