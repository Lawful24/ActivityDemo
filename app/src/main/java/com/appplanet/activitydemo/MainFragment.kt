package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.view.activity_button
import kotlinx.android.synthetic.main.fragment_main.view.editTextTextPersonName

lateinit var communicator: Communicator

private lateinit var recyclerView: RecyclerView

var messages = listOf("This", "is", "a", "RecyclerView")

var recyclerTextView: TextView? = null

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        communicator = activity as Communicator

        fun switchFragment(nextFragment: Fragment) {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, nextFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val textViewClickListener = View.OnClickListener {
            switchFragment(MessageFragment())
        }

        rootView.activity_button.setOnClickListener {
            communicator.passDataCom(rootView.editTextTextPersonName.text.toString())
        }

        // recyclerView declaration
        recyclerView = rootView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MessageAdapter(messages, textViewClickListener)
        recyclerView.setHasFixedSize(true)

        recyclerTextView = rootView.findViewById(R.id.card_title)

        return rootView
    }
}