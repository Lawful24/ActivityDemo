package com.appplanet.activitydemo

import java.util.*

data class Movie (private val _title: String, private val _id: String) {
    val title: String
        get() = _title

    val id: String
        get() = _id
}

val movie1 = Movie("Scott Pilgrim vs. The World", UUID.randomUUID().toString())
val movie2 = Movie("Rush", UUID.randomUUID().toString())
val movie3 = Movie("Bandersnatch", UUID.randomUUID().toString())
val movie4 = Movie("Joker", UUID.randomUUID().toString())
val movie5 = Movie("Tenet", UUID.randomUUID().toString())