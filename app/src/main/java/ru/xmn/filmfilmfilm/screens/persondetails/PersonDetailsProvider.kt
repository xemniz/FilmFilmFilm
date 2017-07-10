package ru.xmn.filmfilmfilm.screens.persondetails

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import mu.KLogging
import ru.xmn.filmfilmfilm.application.di.scopes.ActivityScope
import ru.xmn.filmfilmfilm.common.extensions.logError
import ru.xmn.filmfilmfilm.services.film.FilmData
import ru.xmn.filmfilmfilm.services.film.FilmDataManager
import ru.xmn.filmfilmfilm.services.tmdb.PersonType
import ru.xmn.filmfilmfilm.services.tmdb.TmdbManager
import javax.inject.Inject

@ActivityScope
class PersonDetailsProvider @Inject
constructor(val filmDataManager: FilmDataManager, val tmdb: TmdbManager) {
    companion object : KLogging()

    fun getMoviesForPerson(personType: PersonType, personId: String) =
            tmdb.getMoviesForPerson(personType, personId)
                    .doOnNext {
                        Observable.fromCallable({ filmDataManager.updateFilmData(it) })
                                .filter { it != null }
                                .map {
                                    filmDataManager.updateRatings(it!!)
                                    filmDataManager.updateCredits(it)
                                    filmDataManager.updateTrailer(it)
                                }
                                .subscribeOn(Schedulers.io())
                                .subscribe({}, { logError(logger, "fun getMoviesForPerson(personType: PersonType, personId: String)", it) })
                    }
                    .map { it.id.toString() }
                    .toList()


    fun subscribeToFilms(ids: List<String?>?): Observable<List<FilmData>> = filmDataManager.subscribeToFilms("tmdbId", ids)
}
