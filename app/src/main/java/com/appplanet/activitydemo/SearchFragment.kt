package com.appplanet.activitydemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appplanet.activitydemo.databinding.FragmentSearchBinding
import com.appplanet.activitydemo.network.ServerResponseListener
import com.appplanet.activitydemo.network.controller.MovieController
import com.appplanet.activitydemo.network.model.Movie
import kotlinx.android.synthetic.main.card_layout.view.card_title
import kotlinx.android.synthetic.main.fragment_search.view.search_bar
import java.util.Timer
import java.util.TimerTask

class SearchFragment : Fragment(), OnItemClickedListener {

    // initialise view binding
    private var viewBinding: FragmentSearchBinding? = null

    private lateinit var recyclerView: RecyclerView
    private var recyclerTextView: TextView? = null

    private lateinit var movieController: MovieController
    private lateinit var adapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // view binding
        viewBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return viewBinding!!.apply {

            // controller declaration
            movieController = MovieController()

            // adapter declaration
            adapter = MessageAdapter(emptyList(), this@SearchFragment)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialises the search bar EditText
        viewBinding!!.searchBar.addTextChangedListener(initSearchBarListener())

        // initializes the RecyclerView
        initRecyclerView()
    }

    private fun initSearchBarListener(): TextWatcher {
        return object : TextWatcher {
            private var timer: Timer = Timer()
            private val delayMs = 500L

            override fun afterTextChanged(searchBarText: Editable) {
                timer.cancel()
                timer = Timer()
                if (searchBarText.isNotEmpty()) {
                    timer.schedule(object : TimerTask() {
                        override fun run() {

                            // makes api call on a background thread
                            if (viewBinding!!.root.search_bar.text.isNotEmpty()) {

                                // gets text from EditText
                                makeApiCallOnNewThread(viewBinding!!.root.search_bar.text.toString())
                            }
                        }
                    }, delayMs)
                }
            }

            override fun beforeTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {
                // no-op
            }
        }
    }

    private fun makeApiCallOnNewThread(query: String) {
        val apiThread = Thread {
            movieController.searchMovies(query, object : ServerResponseListener {
                override fun getResult(results: List<Movie>?) {
                    if (results != null) {
                        adapter.setMoviesList(results)
                    } else {
                        requireActivity().runOnUiThread(Runnable {
                            Toast.makeText(context, "An error has occurred.", Toast.LENGTH_LONG)
                                .show()
                        })
                    }
                }
            })
        }
        apiThread.run()
    }

    private fun initRecyclerView() {
        recyclerView = viewBinding!!.recyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }

        // pass the movie list to the adapter
        recyclerView.adapter = adapter
        recyclerTextView = viewBinding!!.root.card_title
    }

    override fun onItemClicked(listItem: Movie) {
        val movieFragment = MovieDetailsFragment.getInstance(listItem)

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.run {
            replace(R.id.fragment_container, movieFragment)
            addToBackStack(null)
            commit()
        }
    }
}

interface OnItemClickedListener {
    fun onItemClicked(item: Movie)
}