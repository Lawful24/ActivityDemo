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
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.util.Timer
import java.util.TimerTask

private lateinit var recyclerView: RecyclerView

var recyclerTextView: TextView? = null

class SearchFragment(private val parcelize: Parcelize) : Fragment(), OnItemClickedListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_search, container, false)

    private val movies = MovieResultsFactory.getMovieResults(parcelize)!!.results

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<EditText>(R.id.search_bar).addTextChangedListener(initSearchBarListener())

        // recyclerView declaration
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
        recyclerView.adapter = MessageAdapter(movies, this)
        recyclerTextView = view.findViewById(R.id.card_title)
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

    override fun onItemClicked(item: Movie) {
        val movieFragment = MovieDetailsFragment(item)
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