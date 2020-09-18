package com.appplanet.activitydemo

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    @Json(name = "total_results") val releaseDate: Int,
    val title: String,
    @Json(name = "vote_average") val voteAverage: Double
) : Parcelable