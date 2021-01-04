package com.appplanet.activitydemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appplanet.activitydemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialise view binding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, SearchFragment())
        transaction.commit()
    }
}