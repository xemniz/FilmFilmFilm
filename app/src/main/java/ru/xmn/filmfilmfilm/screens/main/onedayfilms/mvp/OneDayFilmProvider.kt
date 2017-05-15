package ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import khronos.*
import ru.xmn.filmfilmfilm.application.di.scopes.FragmentScope
import ru.xmn.filmfilmfilm.common.findFirstRegex
import ru.xmn.filmfilmfilm.common.timeStamp
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.viewmodels.FilmItemViewModel
import ru.xmn.filmfilmfilm.servises.KudaGoService
import ru.xmn.filmfilmfilm.servises.OmdbService
import ru.xmn.filmfilmfilm.servises.data.Movie
import ru.xmn.filmfilmfilm.servises.data.OmdbResponse
import ru.xmn.filmfilmfilm.servises.data.toViewModel
import javax.inject.Inject

@FragmentScope
class OneDayFilmProvider @Inject
constructor(val kudaGo: KudaGoService, val omdb: OmdbService) {
    fun getMovies(addDays: Int = 0): Single<MutableList<FilmItemViewModel>>? {
        val since = (Dates.now + addDays.days).beginningOfDay
        val until = since.endOfDay
        return kudaGo
                .getMovies(actualSince = since.timeStamp(), actualUntil = until.timeStamp())
                .flatMap { Observable.fromIterable(it.results) }
                .flatMap { getOmdbInfo(it) }
                .map { it.toViewModel() }
                .toList()
    }

    private fun getOmdbInfo(movie: Movie): Observable<Pair<Movie, OmdbResponse?>> {
        val imdbid = """tt\d+""".toRegex().find(movie.imdb_url)?.value
        return when (imdbid) {
            null -> Observable.just(Pair<Movie, OmdbResponse?>(movie, null))
            else -> omdb.getMovieInfo(imdbid)
                    .map { Pair<Movie, OmdbResponse?>(movie, it) }
                    .subscribeOn(Schedulers.io())
        }
    }
}