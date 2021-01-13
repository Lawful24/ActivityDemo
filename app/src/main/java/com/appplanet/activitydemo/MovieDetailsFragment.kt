package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appplanet.activitydemo.databinding.FragmentMovieDetailsBinding
import com.appplanet.activitydemo.network.controller.MovieController
import com.appplanet.activitydemo.network.model.Movie
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_movie_details.view.textView

const val MOVIE_PARCELABLE_KEY = "movie_key"

class MovieDetailsFragment : Fragment() {

    // initialise view binding
    private var viewBinding: FragmentMovieDetailsBinding? = null

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        return viewBinding!!.apply {
            MovieController().getMovieById(arguments?.getInt(MOVIE_PARCELABLE_KEY))
                .doOnSuccess {
                    viewBinding!!.root.textView.text = it.title
                }
                .doOnSuccess {

                }
                .subscribe()
        }.root
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