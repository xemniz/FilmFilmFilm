package ru.xmn.filmfilmfilm.services

import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmItemViewData
import ru.xmn.filmfilmfilm.services.kudago.Movie
import ru.xmn.filmfilmfilm.services.omdb.OmdbResponse
import ru.xmn.filmfilmfilm.services.tmdb.TmdbMovieInfo
import ru.xmn.filmfilmfilm.services.tmdb.pathToUrl

fun createViewData(movie: Movie, omdbResponse: OmdbResponse?): FilmItemViewData =
        FilmItemViewData(movie.imdbId, null,
                movie.title, movie.poster.image, movie.director,
                movie.genres.map { it.name },
                omdbResponse?.Ratings?.associateBy({ it.Source }, { it.Value }) ?: HashMap<String, String>()
        )

fun createViewData(movie: TmdbMovieInfo, omdbResponse: OmdbResponse?): FilmItemViewData =
        FilmItemViewData(movie.imdb_id, movie.id.toString(),
                movie.title, movie.poster_path.pathToUrl(), "",
                movie.genres.map { it.name },
                omdbResponse?.Ratings?.associateBy({ it.Source }, { it.Value }) ?: HashMap<String, String>()
        )

fun createViewData(movie: TmdbMovieInfo): FilmItemViewData =
        FilmItemViewData(movie.imdb_id,movie.id.toString(),
                movie.title, movie.poster_path.pathToUrl(), "",
                emptyList(),
                HashMap<String, String>())

val Movie.imdbId: String?
    get() = """tt\d+""".toRegex().find(this.imdb_url)?.value
