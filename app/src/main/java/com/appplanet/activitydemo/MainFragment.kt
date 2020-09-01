package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.editTextTextPersonName
import kotlinx.android.synthetic.main.fragment_main.view.activity_button

private lateinit var recyclerView: RecyclerView

var movies = listOf(movie1, movie2, movie3, movie4, movie5)

var recyclerTextView: TextView? = null

class MainFragment : Fragment(), OnItemClickedListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        fun switchFragment(nextFragment: Fragment) {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, nextFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        rootView.activity_button.setOnClickListener {
            switchFragment(MovieDetailsFragment(editTextTextPersonName.text.toString()))
        }

        // recyclerView declaration
        recyclerView = rootView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MessageAdapter(movies, this)
        recyclerView.setHasFixedSize(true)

        recyclerTextView = rootView.findViewById(R.id.card_title)

        return rootView
    }

    override fun onItemClicked(item: Movie) {
        val movieFragment = MovieDetailsFragment(item.title)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, movieFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

interface OnItemClickedListener {
    fun onItemClicked(item: Movie)
}