package com.appplanet.activitydemo.network.controller

import com.appplanet.activitydemo.BuildConfig
import com.appplanet.activitydemo.network.NetworkSingleton
import com.appplanet.activitydemo.network.api.TmdbService
import com.appplanet.activitydemo.network.model.Movie
import com.appplanet.activitydemo.network.model.MovieResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieController {

    private val retrofit = NetworkSingleton.retrofit

    private val tmdbService = retrofit.create(TmdbService::class.java)

    fun searchMovies(query: String): Single<MovieResponse> =
        tmdbService.getMoviesFromQuery(BuildConfig.MOVIE_API_KEY, query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    fun getMostPopularMovies(): Single<MovieResponse> =
        tmdbService.getMostPopularMovies(BuildConfig.MOVIE_API_KEY)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    fun getMovieById(movieId: Int?): Single<Movie> =
        tmdbService.getMovieById(movieId, BuildConfig.MOVIE_API_KEY)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}
