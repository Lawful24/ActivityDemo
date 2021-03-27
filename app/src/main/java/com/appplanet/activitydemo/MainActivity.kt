package com.appplanet.activitydemo

import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.appplanet.activitydemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialise view binding
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)

        // instantiate navHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // instantiate ConnectivityManager
        val connectivityManager = getSystemService(ConnectivityManager::class.java)

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.e(TAG, "The default network is now: $network")

                navController.navigate(
                    R.id.searchFragment
                )
            }

            override fun onUnavailable() {
                Log.e(
                    TAG, "Unavailable"
                )

                navController.navigate(
                    R.id.connectionProblemFragment
                )
            }

            override fun onLost(network: Network) {
                Log.e(
                    TAG,
                    "The application no longer has a default network. The last default network was $network"
                )

                navController.navigate(
                    R.id.connectionProblemFragment
                )
            }
        })
    }
}