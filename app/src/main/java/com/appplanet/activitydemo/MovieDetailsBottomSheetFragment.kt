package com.appplanet.activitydemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appplanet.activitydemo.network.model.Movie
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_share.textShareExternally
import kotlinx.android.synthetic.main.bottom_sheet_share.textShareViaEmail

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

        initEmailButton(arguments?.getInt(MOVIE_PARCELABLE_KEY))
        initShareExtButton(arguments?.getInt(MOVIE_PARCELABLE_KEY))
    }

    private fun initEmailButton(movieId: Int?) {
        val shareEmailIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.emailSubject))
            putExtra(
                Intent.EXTRA_TEXT,
                context?.getString(
                    R.string.linkShareTextTemplate,
                    getString(R.string.linkShareText),
                    TMDB_MOVIE_PAGE_URL_TEMPLATE + movieId
                )
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // for the "back" button to navigate back to the app
        }

        if (activity?.packageManager?.queryIntentActivities(shareEmailIntent, 0)?.size != 0) {
            textShareViaEmail.setOnClickListener {
                startActivity(Intent.createChooser(shareEmailIntent, null))
            }
        } else {
            textShareViaEmail.text = getString(R.string.noEmailAppString)
            textShareViaEmail.setTextColor(resources.getColor(R.color.red))
        }
    }

    private fun initShareExtButton(movieId: Int?) {
        val shareExtIntent = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                context?.getString(
                    R.string.linkShareTextTemplate,
                    getString(R.string.linkShareText),
                    TMDB_MOVIE_PAGE_URL_TEMPLATE + movieId
                )
            )
            type = "text/plain"
        }, null)

        textShareExternally.setOnClickListener {
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