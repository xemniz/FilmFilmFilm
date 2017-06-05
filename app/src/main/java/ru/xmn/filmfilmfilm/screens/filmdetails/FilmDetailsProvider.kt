package ru.xmn.filmfilmfilm.screens.filmdetails

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.xmn.filmfilmfilm.application.di.scopes.ActivityScope
import ru.xmn.filmfilmfilm.services.kudago.KudaGoManager
import ru.xmn.filmfilmfilm.services.omdb.TmdbManager
import ru.xmn.filmfilmfilm.services.tmdb.TmdbCredits
import ru.xmn.filmfilmfilm.services.tmdb.TmdbMovieInfo
import javax.inject.Inject

@ActivityScope
class FilmDetailsProvider
@Inject
constructor(val kudaGo: KudaGoManager, val tmdb: TmdbManager) {

    fun getTmdbMovieInfo(imdbId: String) =
            Observable.zip(tmdb.getTmdbMovieInfo(imdbId), tmdb.getTmdbCredits(imdbId),
                    BiFunction<TmdbMovieInfo, TmdbCredits, Pair<TmdbMovieInfo, TmdbCredits>> { info, credits -> Pair(info, credits) })
}