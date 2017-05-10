package ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp

import io.reactivex.Observable
import khronos.*
import ru.xmn.filmfilmfilm.application.di.scopes.FragmentScope
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.viewmodels.FilmItemViewModel
import ru.xmn.filmfilmfilm.servises.KudaGoService
import ru.xmn.filmfilmfilm.servises.data.KudaGoMoviesResponse
import ru.xmn.filmfilmfilm.servises.data.toViewModel
import javax.inject.Inject

@FragmentScope
class OneDayFilmProvider @Inject
constructor(val kudaGo: KudaGoService) {
    fun getMovies(addDays: Int = 0): Observable<List<FilmItemViewModel>> {
        val since = (Dates.now + addDays.days).beginningOfDay
        val until = since.endOfDay
        return kudaGo
                .getMovies(actualSince = since.time / 1000, actualUntil = until.time / 1000)
                .map { it.results.map { it.toViewModel() } }
    }
}