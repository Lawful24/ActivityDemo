package com.appplanet.activitydemo

import android.content.res.AssetManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream

var jsonText: String = ""

class MainActivity : AppCompatActivity(), JsonInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val assetManager: AssetManager = applicationContext.assets
        val inputStream: InputStream = assetManager.open("results.json")
        jsonText = inputStream.bufferedReader().use {
            it.readText()
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, SearchFragment(this))
        transaction.commit()
    }

    override fun getJson() : String {
        return jsonText
    }
}

interface JsonInterface {
    fun getJson() : String
}