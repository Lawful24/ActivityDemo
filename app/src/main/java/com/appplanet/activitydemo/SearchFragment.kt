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
import java.util.Timer
import java.util.TimerTask

private lateinit var recyclerView: RecyclerView

val movies = MovieFactory.getMovies()

var recyclerTextView: TextView? = null

class SearchFragment : Fragment(), OnItemClickedListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_search, container, false)

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
                if (s.isNotEmpty()) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(object: TimerTask() {
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

    private fun switchFragment(nextFragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onItemClicked(item: Movie) {
        val movieFragment = MovieDetailsFragment(item.title)
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