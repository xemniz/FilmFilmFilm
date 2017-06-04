package ru.xmn.filmfilmfilm.screens.main.films.viewmodels

data class FilmItemViewModel(val  imdbId: String?, val title: String, val image: String, val director: String, val genres: List<String>, val Ratings: Map<String, String>)