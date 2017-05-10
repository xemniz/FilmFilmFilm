package ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp

import ru.xmn.filmfilmfilm.common.ui.BaseView
import ru.xmn.filmfilmfilm.servises.data.KudaGoMoviesResponse
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.viewmodels.FilmItemViewModel

/**
 * Created by xmn on 30.04.2017.
 */
interface OneDayFilmView: BaseView {
    fun showMovies(films: List<FilmItemViewModel>)
}