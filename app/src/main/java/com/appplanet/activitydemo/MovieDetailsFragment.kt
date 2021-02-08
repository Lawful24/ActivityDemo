package com.appplanet.activitydemo

import android.content.Intent
import android.content.pm.PackageManager
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
import kotlinx.android.synthetic.main.fragment_movie_details.view.bottomSheetButton
import kotlinx.android.synthetic.main.fragment_movie_details.view.movieTitleTextview
import kotlinx.android.synthetic.main.fragment_movie_details.view.videoButton

const val MOVIE_PARCELABLE_KEY = "movie_key"

const val YOUTUBE_SITE_NAME = "YouTube"
const val VIMEO_SITE_NAME = "Vimeo"
const val YOUTUBE_VIDEO_URL_TEMPLATE = "https://www.youtube.com/watch?v="
const val VIMEO_VIDEO_URL_TEMPLATE = "https://vimeo.com/"
const val YOUTUBE_APP_PACKAGE_NAME = "com.google.android.youtube"

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

        // view binding
        viewBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return viewBinding!!.apply {

            // controller declaration
            movieController = MovieController()
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // subscribes to GET movie by id stream
        disposables.add(fetchMovieById(arguments?.getInt(MOVIE_PARCELABLE_KEY)))

        // subscribes to GET movie videos (by id) stream
        disposables.add(fetchMovieVideosById(arguments?.getInt(MOVIE_PARCELABLE_KEY)))
    }

    override fun onDestroyView() {
        // unsubscribes from all streams
        disposables.clear()

        super.onDestroyView()
    }

    private fun fetchMovieById(movieId: Int?): Disposable {
        return movieController.getMovieById(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                viewBinding!!.root.movieTitleTextview.text = it.title
                initBottomSheetButton(it)
            }
            .doOnError {
                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(
                        context,
                        getString(R.string.detailsFragmentError),
                        Toast.LENGTH_LONG
                    )
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

                    val firstResult = it.results.first()
                    val videoIntent = Intent(Intent.ACTION_VIEW)

                    // this when opens options for implementing more video player apps in the future
                    when (firstResult.site) {
                        YOUTUBE_SITE_NAME -> {

                            // try-catch block for checking whether the target app is installed or not
                            val isYouTubeInstalled = try {
                                context?.packageManager
                                    ?.getPackageInfo(YOUTUBE_APP_PACKAGE_NAME, 0)
                                true
                            } catch (e: PackageManager.NameNotFoundException) {
                                false
                            }

                            videoIntent.data =
                                Uri.parse(YOUTUBE_VIDEO_URL_TEMPLATE + firstResult.key)

                            if (isYouTubeInstalled) {
                                videoIntent.setPackage(YOUTUBE_APP_PACKAGE_NAME)
                            }
                        }

                        VIMEO_SITE_NAME -> {
                            videoIntent.data =
                                Uri.parse(VIMEO_VIDEO_URL_TEMPLATE + firstResult.key)
                        }
                    }

                    // starts the navigation to the external video player app or browser
                    viewBinding!!.root.videoButton.setOnClickListener {
                        startActivity(videoIntent)
                    }
                } else {
                    Log.i(TAG, "NOT FOUND")

                    viewBinding!!.root.videoButton.setOnClickListener {
                        Toast.makeText(
                            context,
                            getString(R.string.noMovieVideosError),
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

    private fun initBottomSheetButton(fetchedMovie: Movie) {
        viewBinding!!.root.bottomSheetButton.setOnClickListener {
            requireActivity().supportFragmentManager.let {
                MovieDetailsBottomSheetFragment.newInstance(fetchedMovie).apply {
                    show(it, tag)
                }
            }
        }
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