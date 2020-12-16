package com.appplanet.activitydemo.network.controller

import android.util.Log
import com.appplanet.activitydemo.BuildConfig
import com.appplanet.activitydemo.network.ServerResponseListener
import com.appplanet.activitydemo.network.model.MovieResponse
import com.appplanet.activitydemo.network.api.TmdbService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type

class MovieController {

    fun searchMovies(query: String, listener: ServerResponseListener) {
        val moshi = Moshi.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(OkHttpClient())
            .build()

        val tmdbService = retrofit.create(TmdbService::class.java)

        tmdbService.getMoviesFromQuery(BuildConfig.MOVIE_API_KEY, query)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    val movieResponse = response.body()
                    val movieListFromResponse =
                        movieResponse!!.results // todo: do research on safe and non-null asserted calls
                    listener.getResult(movieListFromResponse)
                    // called the interface after the response was handled
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e(MovieController::class.java.simpleName, t.message)
                }
            })
    }
}
