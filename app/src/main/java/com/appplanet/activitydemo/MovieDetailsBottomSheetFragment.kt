package com.appplanet.activitydemo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appplanet.activitydemo.network.model.Movie
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_share.text_share_externally

const val TMDB_MOVIE_PAGE_URL_TEMPLATE = "https://www.themoviedb.org/movie/"

class MovieDetailsBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initShareExtButton(arguments?.getInt(MOVIE_PARCELABLE_KEY))
    }

    private fun initShareExtButton(movieId: Int?) {
        val shareExtIntent = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND

            // hardcoding this value until i figure out how to set up multiple languages
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this movie: " + (TMDB_MOVIE_PAGE_URL_TEMPLATE + movieId)
            )
            type = "text/plain"
        }, null)

        text_share_externally.setOnClickListener {
            startActivity(shareExtIntent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(movie: Movie): MovieDetailsBottomSheetFragment {
            val args = Bundle()
            args.putInt(MOVIE_PARCELABLE_KEY, movie.id)
            val fragment = MovieDetailsBottomSheetFragment()
            fragment.arguments = args
            return fragment
        }
    }
}