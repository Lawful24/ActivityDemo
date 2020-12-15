package com.appplanet.activitydemo

import android.content.res.AssetManager
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
import com.appplanet.activitydemo.network.api.TmdbService
import com.appplanet.activitydemo.network.model.Movie
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import java.io.InputStream
import java.util.Timer
import java.util.TimerTask

var jsonText: String = ""

class SearchFragment : Fragment(), OnItemClickedListener {

    private lateinit var recyclerView: RecyclerView
    private var recyclerTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        importJson()
        val movies = MovieResultsFactory.getMovieResults()!!.results

        // establish connection with server

        view.findViewById<EditText>(R.id.search_bar).addTextChangedListener(initSearchBarListener())

        // get text from the EditText

        // should pass a list to the adapter:
        // val moviesFromQuery

        initRecyclerView(view, movies)
    }

    private fun importJson() {
        val importThread = Thread {
            val assetManager: AssetManager = requireActivity().assets
            val inputStream: InputStream = assetManager.open("results.json")
            jsonText = inputStream.bufferedReader().use {
                it.readText()
            }
        }
        importThread.start()
    }

    // todo: find out how to do this properly and find a better name for this method
    private fun initConnection() {

    }

    private fun initConnectionRetrofit() {

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

    private fun initRecyclerView(v: View, movies: List<Movie>) {
        recyclerView = v.findViewById(R.id.recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
        recyclerView.adapter = MessageAdapter(movies, this)
        recyclerTextView = v.findViewById(R.id.card_title)
    }

    override fun onItemClicked(item: Movie) {
        val movieFragment = MovieDetailsFragment.getInstance(item.id)

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