package com.appplanet.activitydemo.network.controller

import android.util.Log
import com.appplanet.activitydemo.network.ServerResponseListener
import com.appplanet.activitydemo.network.model.MovieResponse
import com.appplanet.activitydemo.network.api.TmdbService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MovieController {

    fun searchMovies(query: String, listener: ServerResponseListener) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .build()

        val tmdbService = retrofit.create(TmdbService::class.java)

        tmdbService.getMoviesFromQuery().enqueue(object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val movieResponse = response.body()
                val movieListFromResponse = movieResponse?.results
                listener.getResult(movieListFromResponse)
                // called the interface after the response was handled
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e(MovieController::class.java.simpleName, t.message)
            }
        })
    }
}
