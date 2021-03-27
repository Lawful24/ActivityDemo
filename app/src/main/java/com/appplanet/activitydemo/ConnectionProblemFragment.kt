package com.appplanet.activitydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appplanet.activitydemo.databinding.FragmentConnectionProblemBinding

class ConnectionProblemFragment : Fragment() {
    private var viewBinding: FragmentConnectionProblemBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentConnectionProblemBinding.inflate(inflater, container, false)
        return viewBinding!!.root
    }
}