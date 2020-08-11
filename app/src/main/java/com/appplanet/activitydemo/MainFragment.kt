package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.view.activity_button
import kotlinx.android.synthetic.main.fragment_main.view.editTextTextPersonName

lateinit var communicator: Communicator

private lateinit var recyclerView: RecyclerView

var messages = listOf("This", "is", "a", "RecyclerView")

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MessageAdapter(messages)
        recyclerView.setHasFixedSize(true)

        communicator = activity as Communicator

        rootView.activity_button.setOnClickListener {
            communicator.passDataCom(rootView.editTextTextPersonName.text.toString())
        }
        return rootView
    }
}