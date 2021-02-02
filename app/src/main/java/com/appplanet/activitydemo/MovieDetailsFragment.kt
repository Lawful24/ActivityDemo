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
import kotlinx.android.synthetic.main.fragment_movie_details.view.bottom_sheet_button
import kotlinx.android.synthetic.main.fragment_movie_details.view.textView
import kotlinx.android.synthetic.main.fragment_movie_details.view.video_button

const val MOVIE_PARCELABLE_KEY = "movie_key"

const val YOUTUBE_SITE_NAME = "YouTube"
const val VIMEO_SITE_NAME = "Vimeo"
const val YOUTUBE_URL_TEMPLATE = "https://www.youtube.com/watch?v="
const val VIMEO_URL_TEMPLATE = "https://vimeo.com/"
const val YOUTUBE_PACKAGE_NAME = "com.google.android.youtube"

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

            initBottomSheetButton()
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

                    val firstResult = it.results[0]
                    val packageManager = context?.packageManager
                    val intent = Intent(Intent.ACTION_VIEW)

                    // this when opens options for implementing more video player apps in the future
                    when (firstResult.site) {
                        YOUTUBE_SITE_NAME -> {

                            // try-catch block for checking whether the target app is installed or not
                            val isYouTubeInstalled = try {
                                packageManager?.getPackageInfo(YOUTUBE_PACKAGE_NAME, 0)
                                true
                            } catch (e: PackageManager.NameNotFoundException) {
                                false
                            }

                            intent.data = Uri.parse(YOUTUBE_URL_TEMPLATE + firstResult.key)

                            if (isYouTubeInstalled) {
                                intent.setPackage(YOUTUBE_PACKAGE_NAME)
                            }
                        }

                        VIMEO_SITE_NAME -> {
                            intent.data = Uri.parse(VIMEO_URL_TEMPLATE + firstResult.key)
                        }
                    }

                    // starts the navigation to the external video player app or browser
                    viewBinding!!.root.video_button.setOnClickListener {
                        startActivity(intent)
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

    private fun initBottomSheetButton() {
        viewBinding!!.root.bottom_sheet_button.setOnClickListener {
            requireActivity().supportFragmentManager.let {
                MovieDetailsBottomSheetFragment.newInstance(Bundle()).apply {
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