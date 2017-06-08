package ru.xmn.filmfilmfilm.screens.main.films.viewmodels

import io.realm.RealmList
import io.realm.RealmObject

open class FilmData: RealmObject(){
    val imdbId: String? = null
    val tmdbId: String? = null
    val title: String? = null
    val image: String? = null
    val director: String? = null
    val genres: RealmList<GenreData>? = null
    val ratings: RealmList<RatingData>? = null
    val persons: RealmList<PersonData>? = null
    val sources: RealmList<SourceData>? = null
}

open class PersonData: RealmObject(){
    var id: String? = null
    var name: String? = null
    var descr: String? = null
    var type: String? = null
}

open class RatingData: RealmObject(){
    var id: String? = null
    var name: String? = null
    var descr: String? = null
    var type: String? = null
}

open class GenreData: RealmObject(){
    var name: String? = null
}

open class SourceData: RealmObject(){
    var name: String? = null
    var timestamp: String? = null
}