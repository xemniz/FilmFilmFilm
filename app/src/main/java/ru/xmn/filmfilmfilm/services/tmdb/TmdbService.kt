package ru.xmn.filmfilmfilm.services.omdb

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import ru.xmn.filmfilmfilm.services.tmdb.TmdbCredits
import ru.xmn.filmfilmfilm.services.tmdb.TmdbMovieInfo

interface TmdbService {

    @GET("/3/movie/{movie_id}")
    fun getMovieInfo(@Path("movie_id") imdbId: String)
            : Observable<TmdbMovieInfo>

    @GET("/3/movie/{movie_id}/credits")
    fun getMovieCredits(@Path("movie_id") imdbId: String)
            : Observable<TmdbCredits>
}
