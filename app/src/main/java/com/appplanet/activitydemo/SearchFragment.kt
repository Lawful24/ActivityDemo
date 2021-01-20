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
import com.appplanet.activitydemo.network.controller.MovieController
import com.appplanet.activitydemo.network.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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

    private val onViewCreatedDisposables = CompositeDisposable()
    private val onStartDisposables = CompositeDisposable()
    private lateinit var searchDisposable: Disposable
    private lateinit var popularDisposable: Disposable

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

    override fun onStart() {
        super.onStart()

        // subscribe to the most popular movies stream
        onStartDisposables.add(mostPopularMoviesCall())
    }

    override fun onStop() {
        // unsubscribe from the popular stream
        onStartDisposables.clear()

        super.onStop()
    }

    override fun onDestroyView() {
        // unsubscribe from the search stream
        onViewCreatedDisposables.clear()

        super.onDestroyView()
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
                                onViewCreatedDisposables.add(
                                    searchMoviesCall(viewBinding!!.root.search_bar.text.toString())
                                )
                            }
                        }
                    }, delayMs)
                } else {
                    mostPopularMoviesCall()
                }
            }

            override fun beforeTextChanged(
                sequence: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                // no-op
            }

            override fun onTextChanged(
                sequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                // no-op
            }
        }
    }

    private fun searchMoviesCall(query: String): Disposable {
        return movieController.searchMovies(query)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                if (it.results.isNotEmpty()) {
                    adapter.setMoviesList(it.results)
                } else {
                    requireActivity().runOnUiThread(Runnable {
                        Toast.makeText(context, "No results found.", Toast.LENGTH_LONG)
                            .show()
                    })
                }
            }
            .doOnError {
                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(context, "An error had occurred.", Toast.LENGTH_LONG)
                        .show()
                })
            }
            .subscribe()
    }

    private fun mostPopularMoviesCall(): Disposable {
        return movieController.getMostPopularMovies()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                if (it.results.isNotEmpty()) {
                    adapter.setMoviesList(it.results)
                } else {
                    requireActivity().runOnUiThread(Runnable {
                        Toast.makeText(context, "No popular movies found.", Toast.LENGTH_LONG)
                            .show()
                    })
                }
            }
            .doOnError {
                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(context, "An error had occurred.", Toast.LENGTH_LONG)
                        .show()
                })
            }
            .subscribe()
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

    override fun onItemClicked(recyclerViewItem: Movie) {
        val movieFragment = MovieDetailsFragment.getInstance(recyclerViewItem)

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.run {
            replace(R.id.fragment_container, movieFragment)
            addToBackStack(null)
            commit()
        }
    }
}

interface OnItemClickedListener {
    fun onItemClicked(recyclerViewItem: Movie)
}