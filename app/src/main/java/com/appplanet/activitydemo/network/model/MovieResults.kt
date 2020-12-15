package com.appplanet.activitydemo.network.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MovieResults(
    val results: List<Movie>,
    @Json(name = "total_results") val totalResults: Int
) : Parcelable