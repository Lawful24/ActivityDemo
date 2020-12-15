package com.appplanet.activitydemo.network

import com.appplanet.activitydemo.network.model.Movie

interface ServerResponseListener {
    fun getResult(results: List<Movie>)
}