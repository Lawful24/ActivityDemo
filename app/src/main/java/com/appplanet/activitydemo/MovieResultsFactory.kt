package com.appplanet.activitydemo

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

object MovieResultsFactory {
    private val moshi: Moshi = Moshi.Builder().build()
    private val adapter: JsonAdapter<MovieResults> = moshi.adapter(MovieResults::class.java)
    private val movieResults = adapter.fromJson(jsonText)

    fun getMovieResults(): MovieResults? {
        return movieResults
    }

    fun findMovieById(movieId: Int): Movie {
        val movies = movieResults!!.results
        var counter = 0
        while (counter < movies.size && movieId != movies[counter].id) {
            if (movieId != movies[counter].id) {
                counter++
            }
        }
        return movies[counter]
    }
}