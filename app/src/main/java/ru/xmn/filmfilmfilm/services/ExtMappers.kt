package ru.xmn.filmfilmfilm.services

import io.realm.RealmList
import khronos.Dates
import ru.xmn.filmfilmfilm.common.realmext.queryFirst
import ru.xmn.filmfilmfilm.common.realmext.save
import ru.xmn.filmfilmfilm.common.timeStamp
import ru.xmn.filmfilmfilm.services.film.FilmData
import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmItemViewData
import ru.xmn.filmfilmfilm.services.film.GenreData
import ru.xmn.filmfilmfilm.services.film.SourceData
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
        FilmItemViewData(movie.imdb_id, movie.id.toString(),
                movie.title, movie.poster_path.pathToUrl(), "",
                emptyList(),
                HashMap<String, String>())

fun updateFilmData(movie: Movie): FilmData {
    val filmData = FilmData().queryFirst { query -> query.equalTo("imdbId", movie.imdbId) } ?: FilmData()
    filmData.apply {
        imdbId = movie.imdbId
        title = movie.title
        image = movie.poster.image
        director = movie.director
        genres = movie.genres.fold(
                RealmList<GenreData>(),
                { list, genre ->
                    list.add(GenreData().apply {
                        name = genre.name
                    })
                    list
                })
        val sourceData = sources.find { it.name == SourceData.kudago }
        if (sourceData != null) sourceData.timestamp = Dates.now.timeStamp()
        else sources.add(SourceData().apply {
            name = SourceData.kudago
            timestamp = Dates.now.timeStamp()
        })
    }.save()
    return filmData
}

val Movie.imdbId: String?
    get() = """tt\d+""".toRegex().find(this.imdb_url)?.value
