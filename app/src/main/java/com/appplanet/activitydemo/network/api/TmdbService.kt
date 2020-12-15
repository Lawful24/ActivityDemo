package com.appplanet.activitydemo.network.api

import com.appplanet.activitydemo.network.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService {
    @GET("search/movie")    // never start the interface definition with a "/"

    fun getMoviesFromQuery(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<MovieResponse>
}