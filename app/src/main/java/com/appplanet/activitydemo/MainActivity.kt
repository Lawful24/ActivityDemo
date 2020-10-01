package com.appplanet.activitydemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

const val apiKey: String = BuildConfig.MOVIE_API_KEY

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Hidden Key", apiKey)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, SearchFragment())
        transaction.commit()
    }
}