package com.appplanet.activitydemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appplanet.activitydemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialise view binding
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, SearchFragment())
        transaction.commit()
    }
}