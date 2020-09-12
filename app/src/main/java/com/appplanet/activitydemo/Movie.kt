package com.appplanet.activitydemo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie (
    @Json(name = "id") val id: Int,
    @Json(name = "release_date") val releaseDate: Int,
    @Json(name = "title") val title: String,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "total_results") val totalResults: Int
)