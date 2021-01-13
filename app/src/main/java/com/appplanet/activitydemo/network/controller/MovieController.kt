package com.appplanet.activitydemo.network.controller

import com.appplanet.activitydemo.BuildConfig
import com.appplanet.activitydemo.network.api.TmdbService
import com.appplanet.activitydemo.network.model.Movie
import com.appplanet.activitydemo.network.model.MovieResponse
import com.squareup.moshi.Moshi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieController {

    private val moshi = Moshi.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(OkHttpClient())
        .build()

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
