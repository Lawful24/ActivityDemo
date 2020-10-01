package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_movie_details.view.textView

class MovieDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_movie_details, container, false)

        val movie = arguments?.getParcelable<Movie>(BuildConfig.MOVIE_API_KEY)
        rootView.textView.text = movie?.title

        return rootView
    }

    companion object {
        fun getInstance(movie: Movie): MovieDetailsFragment {
            val args = Bundle()
            args.putParcelable(apiKey, movie)
            val fragment = MovieDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}