package com.appplanet.activitydemo.network.controller

import android.util.Log
import com.appplanet.activitydemo.BuildConfig
import com.appplanet.activitydemo.network.ServerResponseListener
import com.appplanet.activitydemo.network.api.TmdbService
import com.appplanet.activitydemo.network.model.Movie
import com.squareup.moshi.Moshi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieController {

    val moshi = Moshi.Builder().build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(OkHttpClient())
        .build()

    val tmdbService = retrofit.create(TmdbService::class.java)

    fun searchMovies(query: String, listener: ServerResponseListener<List<Movie>?>) {
        tmdbService.getMoviesFromQuery(BuildConfig.MOVIE_API_KEY, query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result -> listener.getResult(result.results) },
                { throwable -> Log.e(MovieController::class.java.simpleName, throwable.message) })
    }

    fun getMostPopularMovies(listener: ServerResponseListener<List<Movie>?>) {
        tmdbService.getMostPopularMovies(BuildConfig.MOVIE_API_KEY)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { response -> listener.getResult(response.results) },
                { throwable -> Log.e(MovieController::class.java.simpleName, throwable.message) })
    }

    fun getMovieById(movieId: Int?, listener: ServerResponseListener<Movie>) {
        tmdbService.getMovieById(movieId, BuildConfig.MOVIE_API_KEY)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { response -> listener.getResult(response) },
                { throwable -> Log.e(MovieController::class.java.simpleName, throwable.message) }
            )
    }
}
