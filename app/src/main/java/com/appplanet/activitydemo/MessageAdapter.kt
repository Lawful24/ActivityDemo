package com.appplanet.activitydemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appplanet.activitydemo.network.model.Movie

class MessageAdapter(
    private var movieList: List<Movie>,
    private val onItemClickedListener: OnItemClickedListener
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: MessageViewHolder, position: Int) {
        val movieListElement = movieList[position]

        viewHolder.cardText.setOnClickListener {
            onItemClickedListener.onItemClicked(movieListElement)
        }

        viewHolder.setDetailsFragmentData(movieListElement.title)
    }


    override fun getItemCount() = movieList.size

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cardText: TextView = itemView.findViewById(R.id.cardTitle)

        fun setDetailsFragmentData(message: String?) {
            cardText.text = message
        }
    }

    fun setMoviesList(newMovieList: List<Movie>) {
        movieList = newMovieList
        notifyDataSetChanged()  // notifies the RecyclerView about the movieList being modified
    }
}