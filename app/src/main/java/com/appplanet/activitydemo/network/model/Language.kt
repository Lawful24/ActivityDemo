package com.appplanet.activitydemo.network.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Language (
    @Json(name = "iso_639_1") val iso639_1: String,
    @Json(name = "name") val name: String
) : Parcelable