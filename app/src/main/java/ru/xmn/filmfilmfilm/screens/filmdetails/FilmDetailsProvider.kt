package ru.xmn.filmfilmfilm.screens.filmdetails

import ru.xmn.filmfilmfilm.application.di.scopes.ActivityScope
import ru.xmn.filmfilmfilm.services.film.FilmDataManager
import ru.xmn.filmfilmfilm.services.tmdb.TmdbManager
import javax.inject.Inject

@ActivityScope
class FilmDetailsProvider
@Inject
constructor(val filmDataManager: FilmDataManager, val tmdb: TmdbManager) {

    fun getTmdbMovieInfo(imdbId: String) =
                    tmdb.getTmdbMovieInfo(imdbId).doOnNext{filmDataManager.updateFilmData(it)}
}