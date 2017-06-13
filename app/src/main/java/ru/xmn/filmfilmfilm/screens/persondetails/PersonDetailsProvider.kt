package ru.xmn.filmfilmfilm.screens.persondetails

import io.reactivex.Single
import ru.xmn.filmfilmfilm.application.di.scopes.ActivityScope
import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmItemViewData
import ru.xmn.filmfilmfilm.services.createViewData
import ru.xmn.filmfilmfilm.services.film.FilmDataManager
import ru.xmn.filmfilmfilm.services.omdb.OmdbManager
import ru.xmn.filmfilmfilm.services.tmdb.PersonType
import ru.xmn.filmfilmfilm.services.tmdb.TmdbManager
import javax.inject.Inject

@ActivityScope
class PersonDetailsProvider @Inject
constructor(val filmDataManager: FilmDataManager, val tmdb: TmdbManager) {

    fun getMoviesForPerson(personType: PersonType, personId: String) =
            tmdb.getMoviesForPerson(personType, personId)
                    .doOnNext { filmDataManager.updateFilmData(it) }
                    .map { it.id.toString() }
                    .toList()

}
