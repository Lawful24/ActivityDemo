package com.appplanet.activitydemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.appplanet.activitydemo.databinding.FragmentMovieDetailsBinding
import com.appplanet.activitydemo.network.controller.MovieController
import com.appplanet.activitydemo.network.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_movie_details.view.textView
import kotlinx.android.synthetic.main.fragment_movie_details.view.video_button

const val MOVIE_PARCELABLE_KEY = "movie_key"


class MovieDetailsFragment : Fragment() {

    private val TAG = "MovieDetailsFragment"

    // initialise view binding
    private var viewBinding: FragmentMovieDetailsBinding? = null
    private lateinit var movieController: MovieController

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return viewBinding!!.apply {

            // controller declaration
            movieController = MovieController()
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables.add(fetchMovieById(arguments?.getInt(MOVIE_PARCELABLE_KEY)))
        disposables.add(fetchMovieVideosById(arguments?.getInt(MOVIE_PARCELABLE_KEY)))
    }

    override fun onDestroyView() {
        disposables.clear()

        super.onDestroyView()
    }

    private fun fetchMovieById(movieId: Int?): Disposable {
        return movieController.getMovieById(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                viewBinding!!.root.textView.text = it.title
            }
            .doOnError {
                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(context, "Unable to load movie.", Toast.LENGTH_LONG)
                        .show()
                })
            }
            .subscribe()
    }

    private fun fetchMovieVideosById(movieId: Int?): Disposable {
        return movieController.getMovieVideosById(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                if (it.results.isNotEmpty()) {
                    Log.i(TAG, "OK")

                    var firstElementSite = it.results[0].site
                    val firstElementKey = it.results[0].key

                    if (firstElementSite == "YouTube") {
                        firstElementSite = "https://www.youtube.com/watch?v="
                    } else if (firstElementSite == "Vimeo") {
                        firstElementSite = "https://vimeo.com/"
                    }

                    viewBinding!!.root.video_button.setOnClickListener {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(firstElementSite + firstElementKey)
                            )
                        )
                    }
                } else {
                    Log.i(TAG, "NOT FOUND")

                    viewBinding!!.root.video_button.setOnClickListener {
                        Toast.makeText(
                            context,
                            "There are no videos for this movie.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .doOnError {
                Log.e(TAG, "ERROR FETCHING VIDEOS")
            }
            .subscribe()
    }

    companion object {
        fun getInstance(clickedItem: Movie): MovieDetailsFragment {
            val args = Bundle()
            args.putInt(MOVIE_PARCELABLE_KEY, clickedItem.id)
            val fragment = MovieDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}