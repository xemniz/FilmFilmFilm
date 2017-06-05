package ru.xmn.filmfilmfilm.services.omdb

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.xmn.filmfilmfilm.common.realmext.query
import ru.xmn.filmfilmfilm.common.realmext.save
import ru.xmn.filmfilmfilm.services.imdbId
import ru.xmn.filmfilmfilm.services.kudago.Movie

class OmdbManager(private val service: OmdbService) {

    fun getOmdbInfo(movie: Movie): Observable<Pair<Movie, OmdbResponse?>> {
        val ABSENT = Pair<Movie, OmdbResponse?>(movie, null)
        val imdbid: String? = movie.imdbId
        return when (imdbid) {
            null -> Observable.just(ABSENT)
            else -> Observable.concat(getOmdbInfoLocalWithMovie(imdbid, movie), getOmdbInfoNetworkWithMovie(imdbid, movie)).first(ABSENT).toObservable()
        }
    }

    fun getOmdbInfo(imdbid: String): Observable<OmdbResponse> {
        return Observable.concat(getOmdbInfoLocal(imdbid), getOmdbInfoNetwork(imdbid)).first(OmdbResponse(emptyList(), imdbid)).toObservable()
    }

    private fun getOmdbInfoLocalWithMovie(imdbid: String, movie: Movie): Observable<Pair<Movie, OmdbResponse?>> =
            getOmdbInfoLocal(imdbid)
                    .map { Pair<Movie, OmdbResponse?>(movie, it) }

    private fun getOmdbInfoLocal(imdbid: String): Observable<OmdbResponse> {
        return Observable.fromCallable {
            OmdbFilm().query { it.equalTo("imdbID", imdbid) }
        }
                .flatMap { Observable.fromIterable(it) }
                .firstElement()
                .toObservable()
                .map { it.toModel() }
    }

    private fun getOmdbInfoNetworkWithMovie(imdbid: String, movie: Movie): Observable<Pair<Movie, OmdbResponse?>> =
            getOmdbInfoNetwork(imdbid)
                    .map { Pair<Movie, OmdbResponse?>(movie, it) }
                    .onErrorReturnItem(Pair<Movie, OmdbResponse?>(movie, null))
                    .subscribeOn(Schedulers.io())

    private fun getOmdbInfoNetwork(imdbid: String) = service.getMovieInfo(imdbid)
            .doOnNext { it.toRealm().save() }
}
