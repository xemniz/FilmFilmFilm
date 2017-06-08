package ru.xmn.filmfilmfilm.screens.main.films

import io.reactivex.Observable
import io.reactivex.Single
import ru.xmn.filmfilmfilm.application.di.scopes.FragmentScope
import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmItemViewData
import ru.xmn.filmfilmfilm.services.createViewData
import ru.xmn.filmfilmfilm.services.kudago.KudaGoManager
import ru.xmn.filmfilmfilm.services.omdb.OmdbManager
import ru.xmn.filmfilmfilm.services.tmdb.TmdbManager
import javax.inject.Inject

@FragmentScope
class FilmsProvider @Inject
constructor(val kudaGo: KudaGoManager, val omdb: OmdbManager, val tmdb: TmdbManager) {
    fun getMovies(addDays: Int = 0): Single<MutableList<FilmItemViewData>> =
            kudaGo.getMovies(addDays)
                    .flatMap {
                        Observable.fromIterable(it.results)
                                .flatMap { omdb.getOmdbInfo(it) }
                    }
                    .map { createViewData(it.first, it.second) }
                    .toList()
}
