package com.appplanet.activitydemo.network.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "popularity") val popularity: Double,
    @Json(name = "vote_count") val voteCount: Int,
    @Json(name = "video") val video: Boolean,
    @Json(name = "poster_path") var _posterPath: String?,
    @Json(name = "id") val id: Int,
    @Json(name = "adult") val adult: Boolean,
    @Json(name = "backdrop_path") var _backdropPath: String?,
    @Json(name = "original_language") val originalLang: String,
    @Json(name = "original_title") val originalTitle: String,
    @Json(name = "genre_ids") val genreList: Array<Int>,
    @Json(name = "title") val title: String,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "overview") val overview: String,
    @Json(name = "release_date") val releaseDate: String
) : Parcelable {

    // Getters and setters to modify a potential null value in response

    var posterPath: String
        get() = _posterPath ?: ""
        set(value) {
            _posterPath = value
        }

    var backdropPath: String
        get() = _backdropPath ?: ""
        set(value) {
            _backdropPath = value
        }
}