package ru.xmn.filmfilmfilm.services.omdb

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.xmn.filmfilmfilm.common.realmext.query
import ru.xmn.filmfilmfilm.common.realmext.save
import ru.xmn.filmfilmfilm.services.data.*

class OmdbManager(private val service: OmdbService) {

    fun getOmdbInfo(movie: Movie): Observable<Pair<Movie, OmdbResponse?>> {
        val ABSENT = Pair<Movie, OmdbResponse?>(movie, null)
        val imdbid: String? = movie.imdbId
        return when (imdbid) {
            null -> Observable.just(ABSENT)
            else -> Observable.concat(getOmdbInfoLocal(imdbid, movie), getOmdbInfoNetwork(imdbid, movie)).first(ABSENT).toObservable()
        }
    }

    private fun getOmdbInfoLocal(imdbid: String, movie: Movie): Observable<Pair<Movie, OmdbResponse?>> =
            Observable.fromCallable {
                OmdbFilm().query { it.equalTo("imdbID", imdbid) }
            }
                    .flatMap { Observable.fromIterable(it) }
                    .firstElement()
                    .map { Pair<Movie, OmdbResponse?>(movie, it.toModel()) }
                    .toObservable()

    private fun getOmdbInfoNetwork(imdbid: String, movie: Movie): Observable<Pair<Movie, OmdbResponse?>> =
            service.getMovieInfo(imdbid)
                    .doOnNext { it.toRealm().save() }
                    .map { Pair<Movie, OmdbResponse?>(movie, it) }
                    .onErrorReturnItem(Pair<Movie, OmdbResponse?>(movie, null))
                    .subscribeOn(Schedulers.io())
}
