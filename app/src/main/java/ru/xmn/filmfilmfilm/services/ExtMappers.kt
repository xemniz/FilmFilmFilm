package ru.xmn.filmfilmfilm.services

import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmItemViewData
import ru.xmn.filmfilmfilm.services.kudago.Movie
import ru.xmn.filmfilmfilm.services.omdb.OmdbResponse

fun Movie.toViewModel(): FilmItemViewData = this.let { FilmItemViewData(imdbId, title, poster.image, director, genres.map { it.name }, HashMap<String, String>()) }

fun Pair<Movie, OmdbResponse?>.toViewModel(): FilmItemViewData = this.let {
    FilmItemViewData(it.first.imdbId,
            it.first.title, it.first.poster.image, it.first.director,
            it.first.genres.map { it.name },
            it.second?.Ratings?.associateBy({ it.Source }, { it.Value }) ?: HashMap<String, String>()
    )
}

val Movie.imdbId: String?
    get() = """tt\d+""".toRegex().find(this.imdb_url)?.value
