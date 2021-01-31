package com.appplanet.activitydemo.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieVideoResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "results") val results: List<MovieVideo>
)