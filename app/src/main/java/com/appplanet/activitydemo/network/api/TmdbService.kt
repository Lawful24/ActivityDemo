package com.appplanet.activitydemo.network.api

import com.appplanet.activitydemo.network.model.Movie
import com.appplanet.activitydemo.network.model.MovieResponse
import com.appplanet.activitydemo.network.model.MovieVideoResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    // never start the interface definition with a "/"
    @GET("search/movie")
    fun getMoviesFromQuery(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Single<MovieResponse>

    @GET("movie/popular")
    fun getMostPopularMovies(
        @Query("api_key") apiKey: String
    ): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieById(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String
    ): Single<Movie>

    @GET("movie/{movie_id}/videos")
    fun getMovieVideosById(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String
    ): Single<MovieVideoResponse>
}