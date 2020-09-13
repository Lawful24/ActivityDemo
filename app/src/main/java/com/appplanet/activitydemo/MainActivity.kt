package com.appplanet.activitydemo

import android.content.res.AssetManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.io.InputStream

var jsonText: String = ""

class MainActivity : AppCompatActivity(), Parcelize {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsonText = inputJson()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, SearchFragment(this))
        transaction.commit()
    }

    private fun inputJson() : String {
        val assetManager: AssetManager = applicationContext.assets
        val inputStream: InputStream = assetManager.open("results.json")
        return inputStream.bufferedReader().use {
            it.readText()
        }
    }

    override fun getJson() : String {
        return jsonText
    }
}

interface Parcelize {
    fun getJson() : String
}