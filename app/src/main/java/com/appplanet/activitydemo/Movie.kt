package com.appplanet.activitydemo

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Movie(
    val id: Int,
    @Json(name = "release_date") val releaseDate: Int,
    val title: String,
    @Json(name = "vote_average") val voteAverage: Double
) : Parcelable