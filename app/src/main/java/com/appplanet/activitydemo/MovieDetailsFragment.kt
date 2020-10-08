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

        val movieId = arguments?.getInt(BuildConfig.MOVIE_API_KEY)

        rootView.textView.text = getMovieFromList(movieId).title

        return rootView
    }

    companion object {
        fun getInstance(movieId: Int): MovieDetailsFragment {
            val args = Bundle()
            args.putInt(BuildConfig.MOVIE_API_KEY, movieId)
            val fragment = MovieDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    // dumb logic with no regard to an empty list or an unknown id
    private fun getMovieFromList(searchedId: Int?): Movie {
        var counter = 0
        while (counter < movies.size && searchedId != movies[counter].id) {
            if (searchedId != movies[counter].id) {
                counter++
            }

        }
        return movies[counter]
    }
}