package ru.xmn.filmfilmfilm.services.tmdb

import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableFromIterable

class TmdbManager(private val service: TmdbService) {
    fun getTmdbMovieInfo(imdbId: String) = service.getMovieInfo(imdbId)
    fun getTmdbCredits(imdbId: String) = service.getMovieCredits(imdbId)

    //todo возвращать все страницы
    fun getMoviesForPerson(personType: PersonType, personId: String): Observable<TmdbMovieInfo> {
        val moviesForPerson = when (personType) {
            PersonType.CREW -> service.getMoviesForCrew(personId)
            PersonType.CAST -> service.getMoviesForCast(personId)
            else -> service.getMoviesForPerson(personId)
        }
        return moviesForPerson.flatMap { ObservableFromIterable(it.results) }
    }

}

fun String?.pathToUrl() = "https://image.tmdb.org/t/p/w500${this}"

