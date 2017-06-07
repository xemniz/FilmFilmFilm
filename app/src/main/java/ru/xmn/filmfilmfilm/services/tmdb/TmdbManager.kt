package ru.xmn.filmfilmfilm.services.tmdb

class TmdbManager(private val service: TmdbService) {
    fun getTmdbMovieInfo(imdbId: String) = service.getMovieInfo(imdbId)
    fun getTmdbCredits(imdbId: String) = service.getMovieCredits(imdbId)
    fun getMoviesForPerson(personId: String) = service.getMoviesForPerson(personId)
    fun getMoviesForCast(personId: String) = service.getMoviesForCast(personId)
    fun getMoviesForCrew(personId: String) = service.getMoviesForCrew(personId)
}
