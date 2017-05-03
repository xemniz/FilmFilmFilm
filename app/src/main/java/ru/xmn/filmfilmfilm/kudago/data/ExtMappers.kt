package ru.xmn.filmfilmfilm.kudago.data

import ru.xmn.filmfilmfilm.screens.main.onedayfilms.viewmodels.FilmItemViewModel

fun Movie.toViewModel(): FilmItemViewModel = this.let { FilmItemViewModel(title, poster.image, director, genres.map { it.name }) }
