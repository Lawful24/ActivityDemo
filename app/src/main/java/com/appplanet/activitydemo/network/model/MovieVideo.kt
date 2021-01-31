package com.appplanet.activitydemo.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieVideo(
    @Json(name = "id") val id: String,
    @Json(name = "iso_639_1") val iso_639_1: String,
    @Json(name = "iso_3166_1") val iso_3166_1: String,
    @Json(name = "key") val key: String,
    @Json(name = "name") val name: String,
    @Json(name = "site") val site: String,
    @Json(name = "size") val size: Int,
    @Json(name = "type") val type: String,
)