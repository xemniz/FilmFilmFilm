package ru.xmn.filmfilmfilm.screens.main.films

import io.reactivex.Observable
import io.reactivex.Single
import ru.xmn.filmfilmfilm.application.di.scopes.FragmentScope
import ru.xmn.filmfilmfilm.services.film.FilmDataManager
import ru.xmn.filmfilmfilm.services.imdbId
import ru.xmn.filmfilmfilm.services.kudago.KudaGoManager
import javax.inject.Inject

@FragmentScope
class FilmsProvider @Inject
constructor(val kudaGo: KudaGoManager, val filmManager: FilmDataManager) {
    fun getMovies(addDays: Int = 0): Single<MutableList<String?>>? =
            kudaGo.getMovies(addDays)
                    .flatMap { Observable.fromIterable(it.results) }
                    .doOnNext { filmManager.updateFilmData(it) }
                    .filter { it.imdbId!=null }
                    .map { it.imdbId }
                    .toList()
}
