package com.appplanet.activitydemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appplanet.activitydemo.network.model.Movie
import kotlinx.android.synthetic.main.card_layout.view.card_title

class MessageAdapter(
    private val movies: List<Movie>,
    private val onItemClickedListener: OnItemClickedListener
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)

        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val movieListElement = movies[position]

        holder.cardText.setOnClickListener {
            onItemClickedListener.onItemClicked(movieListElement)
        }

        holder.setData(movieListElement.title)
    }

    override fun getItemCount() = movies.size

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cardText: TextView = itemView.card_title

        fun setData(message: String?) {
            cardText.text = message
        }
    }
}