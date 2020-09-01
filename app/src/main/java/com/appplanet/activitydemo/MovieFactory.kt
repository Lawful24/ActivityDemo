package com.appplanet.activitydemo

import java.util.UUID

val movie1 = Movie(UUID.randomUUID().toString(), "Scott Pilgrim vs. The World")
val movie2 = Movie(UUID.randomUUID().toString(), "Rush")
val movie3 = Movie(UUID.randomUUID().toString(), "Bandersnatch")
val movie4 = Movie(UUID.randomUUID().toString(), "Joker")
val movie5 = Movie(UUID.randomUUID().toString(), "Tenet")

object MovieFactory {
    fun getMovies(): MutableList<Movie> {
        var movies: MutableList<Movie> = mutableListOf()
        movies.add(movie1)
        movies.add(movie2)
        movies.add(movie3)
        movies.add(movie4)
        movies.add(movie5)
        return movies
    }
}