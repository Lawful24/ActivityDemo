package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appplanet.activitydemo.databinding.FragmentSearchBinding
import com.appplanet.activitydemo.network.controller.MovieController
import com.appplanet.activitydemo.network.model.Movie
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(), OnItemClickedListener {

    // initialise view binding
    private var viewBinding: FragmentSearchBinding? = null

    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var recyclerView: RecyclerView

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

        progressBar = viewBinding!!.searchProgressBar

        // initialises the search bar EditText
        initSearchBarStream()

        // initializes the RecyclerView
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()

        // subscribe to the most popular movies stream
        onStartDisposables.add(mostPopularMoviesCall())
        onStartDisposables.add(initRedLineView())
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
            RxTextView.afterTextChangeEvents(viewBinding!!.movieSearchBar)
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
            .doOnSubscribe {
                if (!progressBar.isVisible) {
                    progressBar.show()
                }
            }
            .doOnTerminate {
                progressBar.hide()
            }
            .doOnSuccess {
                if (it.results.isNotEmpty()) {
                    adapter.setMoviesList(it.results)
                } else {
                    requireActivity().runOnUiThread(Runnable {
                        Toast.makeText(
                            context,
                            getString(R.string.searchMoviesNoSearchResults),
                            Toast.LENGTH_LONG
                        ).show()
                    })
                }
            }
            .doOnError {
                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(
                        context,
                        getString(R.string.searchFragmentError),
                        Toast.LENGTH_LONG
                    ).show()
                })
            }
            .subscribe()
    }

    private fun mostPopularMoviesCall(): Disposable {
        return movieController.getMostPopularMovies()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate {
                progressBar.hide()
            }
            .doOnSuccess {
                if (it.results.isNotEmpty()) {
                    adapter.setMoviesList(it.results)
                } else {
                    requireActivity().runOnUiThread(Runnable {
                        Toast.makeText(
                            context,
                            getString(R.string.popularMoviesNoResults),
                            Toast.LENGTH_LONG
                        ).show()
                    })
                }
            }
            .doOnError {
                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(
                        context,
                        getString(R.string.searchFragmentError),
                        Toast.LENGTH_LONG
                    ).show()
                })
            }
            .subscribe()
    }

    private fun initRecyclerView() {
        if (!progressBar.isVisible) {
            progressBar.show()
        }

        recyclerView = viewBinding!!.movieRecyclerview
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }

        // pass the movie list to the adapter
        recyclerView.adapter = adapter
    }

    override fun onItemClicked(recyclerViewItem: Movie, textView: View) {
        textView.findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToMovieDetailsFragment(recyclerViewItem)
        )
    }

    private fun initRedLineView(): Disposable {
        return Observable.timer(5L, TimeUnit.SECONDS, Schedulers.io())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewBinding!!.bottomSearchDivider.show()
            }
    }

    private fun View.show() {
        this.visibility = View.VISIBLE
    }

    private fun View.hide() {
        this.visibility = View.INVISIBLE
    }
}

interface OnItemClickedListener {
    fun onItemClicked(recyclerViewItem: Movie, textView: View)
}