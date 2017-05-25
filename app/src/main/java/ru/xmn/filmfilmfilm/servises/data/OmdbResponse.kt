package ru.xmn.filmfilmfilm.servises.data

import io.realm.RealmObject
import khronos.Dates
import java.util.*


class OmdbResponse(
        val Ratings: List<Rating>,
        val imdbID: String
)

class Rating(
        val Source: String,
        val Value: String
)

open class OmdbFilm : RealmObject() {
    var imdbID: String? = null
    var ratings: String? = null
    var createdAt: Date? = null
}

fun OmdbResponse.toRealm(): OmdbFilm = OmdbFilm().apply {
    imdbID = this@toRealm.imdbID
    ratings = this@toRealm.Ratings.map { "${it.Source},${it.Value}" }.joinToString { ";" }
    createdAt = Dates.now
}

fun OmdbFilm.toModel(): OmdbResponse {
    val ratings = this.ratings?.split(";")
            ?.map { Rating(it.split(",")[0], it.split(",")[1]) }
            ?: emptyList()
    return OmdbResponse(ratings, this.imdbID!!)
}
