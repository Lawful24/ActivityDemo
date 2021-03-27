package com.appplanet.activitydemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appplanet.activitydemo.databinding.FragmentLoadingBinding

/*
    Added because the ConnectivityManager couldn't detect
    the connection state before SearchFragment was called.

    That fragment calls api calls so the app crashed when
    those calls were made.

    With this fragment being the nav host fragment, i could avoid
    leading the app straight into a crash at the beginning of runtime.

    But if we start the app with the connection turned off,
    this fragment will show instead of ConnectionErrorFragment.
 */

class LoadingFragment : Fragment() {
    private var viewBinding: FragmentLoadingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLoadingBinding.inflate(inflater, container, false)
        return viewBinding!!.root
    }
}