package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.view.activity_button
import kotlinx.android.synthetic.main.fragment_main.view.editTextTextPersonName // last 2 imports added manually, could not disable wildcards

lateinit var communicator: Communicator

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        communicator = activity as Communicator

        rootView.activity_button.setOnClickListener {
            communicator.passDataCom(rootView.editTextTextPersonName.text.toString())
        }
        return rootView
    }
}