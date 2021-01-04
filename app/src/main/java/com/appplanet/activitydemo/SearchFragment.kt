package com.appplanet.activitydemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appplanet.activitydemo.network.ServerResponseListener
import com.appplanet.activitydemo.network.controller.MovieController
import com.appplanet.activitydemo.network.model.Movie
import kotlinx.android.synthetic.main.fragment_search.search_bar
import java.util.Collections
import java.util.Timer
import java.util.TimerTask

class SearchFragment : Fragment(), OnItemClickedListener {

    private lateinit var recyclerView: RecyclerView
    private var recyclerTextView: TextView? = null
    private lateinit var movieController: MovieController
    private lateinit var adapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        movieController = MovieController()
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialises the search bar EditText
        view.findViewById<EditText>(R.id.search_bar).addTextChangedListener(initSearchBarListener())

        // adapter declaration
        adapter = MessageAdapter(Collections.emptyList<Movie>(), this)

        // initializes the RecyclerView
        initRecyclerView(view)  // no list parameter because the list will be updated anyway
    }

    private fun initSearchBarListener(): TextWatcher {
        return object : TextWatcher {
            private var timer: Timer = Timer()
            private val delayMs = 500L
            private val duration: Int = Toast.LENGTH_SHORT

            override fun afterTextChanged(s: Editable) {
                timer.cancel()
                timer = Timer()
                if (s.isNotEmpty()) {
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            requireActivity().runOnUiThread(Runnable {
                                Toast.makeText(context, s, duration).show()
                            })

                            // makes api call on a background thread
                            if (search_bar.text.isNotEmpty()) {

                                // gets text from EditText
                                makeApiCallOnNewThread(search_bar.text.toString())
                            }

                        }
                    }, delayMs)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // no-op
            }
        }
    }

    private fun makeApiCallOnNewThread(query: String) {
        val apiThread = Thread {
            movieController.searchMovies(query, object : ServerResponseListener {
                override fun getResult(results: List<Movie>) {
                    adapter.setMovies(results)
                }
            })
        }
        apiThread.run()
    }

    private fun initRecyclerView(v: View) {
        recyclerView = v.findViewById(R.id.recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }

        // pass the movie list to the adapter
        recyclerView.adapter = adapter
        recyclerTextView = v.findViewById(R.id.card_title)
    }

    override fun onItemClicked(item: Movie) {
        val movieFragment = MovieDetailsFragment.getInstance(item)

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