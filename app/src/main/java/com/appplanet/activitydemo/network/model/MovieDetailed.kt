package com.appplanet.activitydemo.network.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MovieDetailed(
    @Json(name = "popularity") val popularity: Double,
    @Json(name = "vote_count") val voteCount: Int,
    @Json(name = "video") val video: Boolean,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "id") val id: Int,
    @Json(name = "adult") val adult: Boolean,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "original_language") val originalLang: String,
    @Json(name = "original_title") val originalTitle: String,
    @Json(name = "title") val title: String,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "overview") val overview: String,
    @Json(name = "release_date") val releaseDate: String,

    // details
    @Json(name = "belongs_to_collection") val belongsToCollection: String?, // type?
    @Json(name = "budget") val budget: Int,
    @Json(name = "genres") val genres: List<Genre>,
    @Json(name = "homepage") val homepage: String?,
    @Json(name = "imdb_id") val imdbId: String?,
    @Json(name = "production_companies") val prodCompanies: List<ProdCompany>,
    @Json(name = "production_countries") val prodCountries: List<ProdCountry>,
    @Json(name = "revenue") val revenue: Int,
    @Json(name = "runtime") val runtime: Int?,
    @Json(name = "spoken_languages") val spokenLanguages: List<Language>,
    @Json(name = "status") val status: String,
    @Json(name = "tagline") val tagline: String?,
) : Parcelable