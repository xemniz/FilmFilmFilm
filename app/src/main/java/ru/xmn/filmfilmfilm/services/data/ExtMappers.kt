package ru.xmn.filmfilmfilm.services.data

import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmItemViewModel

fun Movie.toViewModel(): FilmItemViewModel = this.let { FilmItemViewModel(imdbId, title, poster.image, director, genres.map { it.name }, HashMap<String, String>()) }

fun Pair<Movie, OmdbResponse?>.toViewModel(): FilmItemViewModel = this.let {
    FilmItemViewModel(it.first.imdbId,
            it.first.title, it.first.poster.image, it.first.director,
            it.first.genres.map { it.name },
            it.second?.Ratings?.associateBy({ it.Source }, { it.Value }) ?: HashMap<String, String>()
    )
}

val Movie.imdbId: String?
    get() = """tt\d+""".toRegex().find(this.imdb_url)?.value
