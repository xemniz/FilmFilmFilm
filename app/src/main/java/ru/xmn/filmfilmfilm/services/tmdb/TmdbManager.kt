package ru.xmn.filmfilmfilm.services.omdb

class TmdbManager(private val service: TmdbService) {
    fun getTmdbMovieInfo(imdbId: String) = service.getMovieInfo(imdbId)
    fun getTmdbCredits(imdbId: String) = service.getMovieCredits(imdbId)
}
