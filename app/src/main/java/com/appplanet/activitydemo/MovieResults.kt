package com.appplanet.activitydemo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResults(
    val results: List<Movie>,
    @Json(name = "total_results") val totalResults: Int
)