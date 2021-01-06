package com.appplanet.activitydemo.network.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ProdCountry (
    @Json(name = "iso_3166_1") val iso3166_1: String,
    @Json(name = "name") val name: String
) : Parcelable