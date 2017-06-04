package ru.xmn.filmfilmfilm.screens.filmdetails

import io.reactivex.Observable
import io.reactivex.Single
import ru.xmn.filmfilmfilm.application.di.scopes.ActivityScope
import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmItemViewModel
import ru.xmn.filmfilmfilm.services.kudago.KudaGoManager
import ru.xmn.filmfilmfilm.services.omdb.OmdbManager
import ru.xmn.filmfilmfilm.services.data.toViewModel
import javax.inject.Inject

@ActivityScope
class FilmDetailsProvider
@Inject
constructor(val kudaGo: KudaGoManager, val omdb: OmdbManager) {

    fun getMovies(addDays: Int = 0): Single<MutableList<FilmItemViewModel>>? =
            kudaGo.getMovies(addDays)
                    .flatMap {
                        Observable.fromIterable(it.results)
                                .flatMap { omdb.getOmdbInfo(it) }
                    }
                    .map { it.toViewModel() }
                    .toList()
}