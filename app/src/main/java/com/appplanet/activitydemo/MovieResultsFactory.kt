package com.appplanet.activitydemo

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

object MovieResultsFactory {
    private val moshi: Moshi = Moshi.Builder().build()
    private val adapter: JsonAdapter<MovieResults> = moshi.adapter(MovieResults::class.java)

    fun getMovieResults(jsonInterface: JsonInterface): MovieResults? {
        return adapter.fromJson(jsonInterface.getJson())
    }
}