package ru.xmn.filmfilmfilm.services.data

import com.squareup.moshi.Moshi
import io.realm.RealmObject
import khronos.Dates
import ru.xmn.filmfilmfilm.common.listFromJson
import ru.xmn.filmfilmfilm.common.toJson
import java.util.*


data class OmdbResponse(
        val Ratings: List<Rating>,
        val imdbID: String
)

data class Rating(
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
    ratings = this@toRealm.Ratings.toJson()
    createdAt = Dates.now
}

fun OmdbFilm.toModel(): OmdbResponse {
    val ratings = Moshi.Builder().build().listFromJson<Rating>(this.ratings)
    return OmdbResponse(ratings, this.imdbID!!)
}
