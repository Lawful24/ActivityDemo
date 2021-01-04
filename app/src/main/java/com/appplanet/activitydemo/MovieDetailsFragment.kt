package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appplanet.activitydemo.databinding.FragmentMovieDetailsBinding
import com.appplanet.activitydemo.network.model.Movie
import kotlinx.android.synthetic.main.fragment_movie_details.view.textView

class MovieDetailsFragment : Fragment() {

    // initialise view binding
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val movie: Movie? by lazy { arguments?.getParcelable("movie") as Movie? }

        rootView.textView.text = movie?.title // still safe assertion

        return rootView
    }

    companion object {
        fun getInstance(clickedItem: Movie): MovieDetailsFragment {
            val args = Bundle()
            args.putParcelable("movie", clickedItem)
            val fragment = MovieDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}