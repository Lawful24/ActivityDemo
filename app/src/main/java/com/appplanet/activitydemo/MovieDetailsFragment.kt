package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appplanet.activitydemo.network.model.Movie
import kotlinx.android.synthetic.main.fragment_movie_details.view.textView

const val MOVIE_ID_KEY: String = "We live in a twilight world"

class MovieDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_movie_details, container, false)

        val movieId = arguments!!.getInt(MOVIE_ID_KEY)

        rootView.textView.text = findMovieById(movies, movieId).title

        return rootView
    }

    private fun findMovieById(movies: List<Movie>, movieId: Int): Movie {
        var index = 0
        while (index < movies.size && movieId != movies[index].id) {
            if (movieId != movies[index].id) {
                index++
            }
        }
        return movies[index]
    }

    companion object {
        fun getInstance(movieId: Int): MovieDetailsFragment {
            val args = Bundle()
            args.putInt(MOVIE_ID_KEY, movieId)
            val fragment = MovieDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}