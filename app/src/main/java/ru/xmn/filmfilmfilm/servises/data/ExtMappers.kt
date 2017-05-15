package ru.xmn.filmfilmfilm.servises.data

import ru.xmn.filmfilmfilm.screens.main.onedayfilms.viewmodels.FilmItemViewModel

fun Movie.toViewModel(): FilmItemViewModel = this.let { FilmItemViewModel(title, poster.image, director, genres.map { it.name }, HashMap<String, String>()) }

fun Pair<Movie, OmdbResponse?>.toViewModel(): FilmItemViewModel = this.let {
    FilmItemViewModel(
            it.first.title, it.first.poster.image, it.first.director,
            it.first.genres.map { it.name },
            it.second?.Ratings?.associateBy({ it.Source }, { it.Value }) ?: HashMap<String, String>()
    )
}
