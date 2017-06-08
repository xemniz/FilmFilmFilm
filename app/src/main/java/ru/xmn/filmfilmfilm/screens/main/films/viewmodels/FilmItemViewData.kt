package ru.xmn.filmfilmfilm.screens.main.films.viewmodels

data class FilmItemViewData(
        val imdbId: String?,
        val tmdbId: String?,
        val title: String,
        val image: String,
        val director: String,
        val genres: List<String>,
        val Ratings: Map<String, String>)

