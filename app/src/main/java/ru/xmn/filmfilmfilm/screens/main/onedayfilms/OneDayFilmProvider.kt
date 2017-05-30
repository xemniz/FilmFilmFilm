package ru.xmn.filmfilmfilm.screens.main.onedayfilms

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import khronos.*
import ru.xmn.filmfilmfilm.application.di.scopes.FragmentScope
import ru.xmn.filmfilmfilm.common.realmext.query
import ru.xmn.filmfilmfilm.common.realmext.queryOneResultAsObservable
import ru.xmn.filmfilmfilm.common.realmext.save
import ru.xmn.filmfilmfilm.common.timeStamp
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.viewmodels.FilmItemViewModel
import ru.xmn.filmfilmfilm.servises.KudaGoService
import ru.xmn.filmfilmfilm.servises.OmdbService
import ru.xmn.filmfilmfilm.servises.data.*
import javax.inject.Inject

@FragmentScope
class OneDayFilmProvider @Inject
constructor(val kudaGo: KudaGoService, val omdb: OmdbService) {
    fun getMovies(addDays: Int = 0): Single<MutableList<FilmItemViewModel>>? {
        val since = (Dates.now + addDays.days).beginningOfDay
        val until = since.endOfDay
        return kudaGo
                .getMovies(actualSince = since.timeStamp(), actualUntil = until.timeStamp())
                .flatMap {
                    Observable.fromIterable(it.results)
                            .flatMap { getOmdbInfo(it) }
                }
                .map { it.toViewModel() }
                .toList()
    }

    private fun getOmdbInfo(movie: Movie): Observable<Pair<Movie, OmdbResponse?>> {
        val ABSENT = Pair<Movie, OmdbResponse?>(movie, null)
        val imdbid: String? = """tt\d+""".toRegex().find(movie.imdb_url)?.value
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
            omdb.getMovieInfo(imdbid)
                    .doOnNext { it.toRealm().save() }
                    .map { Pair<Movie, OmdbResponse?>(movie, it) }
                    .onErrorReturnItem(Pair<Movie, OmdbResponse?>(movie, null))
                    .subscribeOn(Schedulers.io())
}