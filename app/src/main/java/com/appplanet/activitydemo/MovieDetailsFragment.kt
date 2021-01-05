package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appplanet.activitydemo.databinding.FragmentMovieDetailsBinding
import com.appplanet.activitydemo.network.model.Movie
import kotlinx.android.synthetic.main.fragment_movie_details.view.textView

const val MOVIE_PARCELABLE_KEY = "movie_key"

class MovieDetailsFragment : Fragment() {

    // initialise view binding
    private var binding: FragmentMovieDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        return binding!!.apply {
            val movie: Movie? by lazy { arguments?.getParcelable(MOVIE_PARCELABLE_KEY) as Movie? }
            this.root.textView.text = movie?.title
        }.root
    }

    companion object {
        fun getInstance(clickedItem: Movie): MovieDetailsFragment {
            val args = Bundle()
            args.putParcelable(MOVIE_PARCELABLE_KEY, clickedItem)
            val fragment = MovieDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}