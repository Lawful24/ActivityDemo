package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appplanet.activitydemo.databinding.FragmentMovieDetailsBinding
import com.appplanet.activitydemo.network.ServerResponseListener2
import com.appplanet.activitydemo.network.controller.MovieController
import com.appplanet.activitydemo.network.model.Movie
import com.appplanet.activitydemo.network.model.MovieDetailed
import kotlinx.android.synthetic.main.fragment_movie_details.view.textView

const val MOVIE_PARCELABLE_KEY = "movie_key"

class MovieDetailsFragment : Fragment() {

    // initialise view binding
    private var viewBinding: FragmentMovieDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        return viewBinding!!.apply {

            var textFromMovieCall: String? = ""

            val apiThread = Thread {
                MovieController().getMovieById(
                    arguments?.getInt(MOVIE_PARCELABLE_KEY),
                    object : ServerResponseListener2 {
                        override fun getMovieDetailedResult(result: MovieDetailed?) {
                            textFromMovieCall = result?.title
                            viewBinding!!.root.textView.text = textFromMovieCall
                        }
                    })
            }
            apiThread.run()
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