package com.appplanet.activitydemo.network.controller

import android.util.Log
import com.appplanet.activitydemo.BuildConfig
import com.appplanet.activitydemo.network.ServerResponseListener
import com.appplanet.activitydemo.network.model.MovieResponse
import com.appplanet.activitydemo.network.api.TmdbService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieController {

    val moshi = Moshi.Builder().build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(OkHttpClient())
        .build()

    val tmdbService = retrofit.create(TmdbService::class.java)

    fun searchMovies(query: String, listener: ServerResponseListener) {
        tmdbService.getMoviesFromQuery(BuildConfig.MOVIE_API_KEY, query)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    listener.getResult(response.body()?.results)
                    // called the interface after the response was handled
                }

                override fun onFailure(call: Call<MovieResponse>, throwable: Throwable) {
                    Log.e(MovieController::class.java.simpleName, throwable.message)
                }
            })
    }

    fun getMostPopularMovies(listener: ServerResponseListener) {
        tmdbService.getMostPopularMovies(BuildConfig.MOVIE_API_KEY)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    listener.getResult(response.body()?.results)
                    // called the interface after the response was handled
                }

                override fun onFailure(call: Call<MovieResponse>, throwable: Throwable) {
                    Log.e(MovieController::class.java.simpleName, throwable.message)
                }
            })
    }
}
