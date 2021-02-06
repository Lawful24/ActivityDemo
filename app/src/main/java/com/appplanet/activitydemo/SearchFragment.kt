package com.appplanet.activitydemo

import android.os.Bundle
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
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.card_layout.view.card_title
import kotlinx.android.synthetic.main.fragment_search.search_bar
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(), OnItemClickedListener {

    // initialise view binding
    private var viewBinding: FragmentSearchBinding? = null

    private lateinit var recyclerView: RecyclerView
    private var recyclerTextView: TextView? = null

    private lateinit var movieController: MovieController
    private lateinit var adapter: MessageAdapter

    private val onViewCreatedDisposables = CompositeDisposable()
    private val onStartDisposables = CompositeDisposable()

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
        initSearchBarStream()

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

    private fun initSearchBarStream() {
        onViewCreatedDisposables.add(
            RxTextView.afterTextChangeEvents(search_bar)
                .map {
                    it.view().text.toString()
                }
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe {
                    if (it.isNotEmpty()) {
                        onViewCreatedDisposables.add(searchMoviesCall(it))
                    } else {
                        mostPopularMoviesCall()
                    }
                }
        )
    }

    private fun searchMoviesCall(query: String): Disposable {
        return movieController.searchMovies(query)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                if (it.results.isNotEmpty()) {
                    adapter.setMoviesList(it.results)
                } else {
                    requireActivity().runOnUiThread(Runnable {
                        Toast.makeText(
                            context,
                            getString(R.string.search_movies_no_search_results),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    })
                }
            }
            .doOnError {
                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(
                        context,
                        getString(R.string.search_fragment_error),
                        Toast.LENGTH_LONG
                    )
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
                        Toast.makeText(
                            context,
                            getString(R.string.popular_movies_no_results),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    })
                }
            }
            .doOnError {
                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(
                        context,
                        getString(R.string.search_fragment_error),
                        Toast.LENGTH_LONG
                    )
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