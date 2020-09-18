package com.appplanet.activitydemo

import android.content.res.AssetManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream

var jsonText: String = ""

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bgThread = Thread {
            val assetManager: AssetManager = applicationContext.assets
            val inputStream: InputStream = assetManager.open("results.json")
            jsonText = inputStream.bufferedReader().use {
                it.readText()
            }
        }
        bgThread.start()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, SearchFragment())
        transaction.commit()
    }
}