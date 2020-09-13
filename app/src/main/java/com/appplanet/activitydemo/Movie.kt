package com.appplanet.activitydemo

import com.squareup.moshi.Json

data class Movie (
    val id: Int,
    @Json(name = "total_results") val releaseDate: Int,
    val title: String,
    @Json(name = "vote_average") val voteAverage: Double
)