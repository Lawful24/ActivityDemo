package com.appplanet.activitydemo.network

import com.appplanet.activitydemo.network.model.Movie
import com.appplanet.activitydemo.network.model.MovieDetailed

interface ServerResponseListener {
    fun getResult(results: List<Movie>?)
}

interface ServerResponseListener2 {
    fun getMovieDetailedResult(result: MovieDetailed?)
}