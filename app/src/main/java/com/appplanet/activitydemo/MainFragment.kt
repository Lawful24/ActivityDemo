package com.appplanet.activitydemo

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

private lateinit var sendButton: Button

class MainFragment : Fragment() {
    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        sendButton = rootView.findViewById(R.id.activity_button)

        sendButton.setOnClickListener {
            val editText = rootView.findViewById<EditText>(R.id.editTextTextPersonName)
            val message = editText.text.toString()

            val fragmentManager = fragmentManager
            fragmentManager!!.beginTransaction()
                .replace(R.id.fragment_container, MessageFragment())
                .addToBackStack(null)
                .commit()
        }
        return rootView
    }
}