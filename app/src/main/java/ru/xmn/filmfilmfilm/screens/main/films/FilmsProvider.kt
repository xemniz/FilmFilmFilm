package ru.xmn.filmfilmfilm.screens.main.films

import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import io.realm.RealmResults
import ru.xmn.filmfilmfilm.application.di.scopes.FragmentScope
import ru.xmn.filmfilmfilm.services.film.FilmData
import ru.xmn.filmfilmfilm.services.film.FilmDataManager
import ru.xmn.filmfilmfilm.services.imdbId
import ru.xmn.filmfilmfilm.services.kudago.KudaGoManager
import javax.inject.Inject

@FragmentScope
class FilmsProvider @Inject
constructor(val kudaGo: KudaGoManager, val filmManager: FilmDataManager) {
    fun getMovies(addDays: Int = 0): Single<MutableList<String?>> =
            kudaGo.getMovies(addDays)
                    .flatMap { Observable.fromIterable(it.results) }
                    .doOnNext({
                        val filmData = filmManager.updateFilmData(it)
                        filmData?.let { fd ->
                            filmManager.updateCredits(fd)
                            filmManager.updateFromTmdb(fd)
                            filmManager.updateRatings(fd)
                        }
                    })
                    .filter { it?.imdbId != null }
                    .map { it!!.imdbId }
                    .toList()

    fun subscribeToFilms(ids: List<String?>?): Observable<List<FilmData>> = filmManager.subscribeToFilms("imdbId", ids)
}
