package ru.xmn.filmfilmfilm.services.film

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class FilmData : RealmObject() {
    @PrimaryKey
    var imdbId: String? = null
    var tmdbId: String? = null
    var title: String? = null
    var image: String? = null
    var trailer: String? = null
    var backdrop: String? = null
    var overview: String? = null
    var director: String? = null
    var genres: RealmList<GenreData> = RealmList()
    var ratings: RealmList<RatingData> = RealmList()
    var persons: RealmList<PersonData> = RealmList()
    var sources: RealmList<SourceData> = RealmList()
    override fun toString(): String {
        return "FilmData(title=$title, backdrop=$backdrop, genres=$genres, ratings=$ratings)"
    }


}

open class PersonData : RealmObject() {
    @PrimaryKey
    var tmdbId: String = UUID.randomUUID().toString()
    var name: String? = null
    var descr: String? = null
    var type: String? = null
}

open class RatingData : RealmObject() {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var source: String? = null
    var value: String? = null
}

open class GenreData : RealmObject() {
    @PrimaryKey
    var name: String? = null
}

open class SourceData : RealmObject() {
    @PrimaryKey
    var name: String? = null
    var timestamp: Long? = null

    companion object {
        val kudago = "kudago"
        val omdb = "omdb"
        val tmdb = "tmdb"
        val kudagoPeriod:Long = 3600*24
        val omdbPeriod:Long = 3600*24
        val tmdbPeriod:Long = 3600*24
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as SourceData

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name?.hashCode() ?: 0
    }


}