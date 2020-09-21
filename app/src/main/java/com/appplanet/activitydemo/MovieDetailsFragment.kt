package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_movie_details.view.textView

class MovieDetailsFragment : Fragment() {
    companion object {
        fun getInstance(movie: Movie): MovieDetailsFragment {
            val args = Bundle()
            args.putParcelable("movie_object", movie)
            val fragment = MovieDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_movie_details, container, false)

        val movie = arguments?.getParcelable<Movie>("movie_object")
        rootView.textView.text = movie?.title

        return rootView
    }
}