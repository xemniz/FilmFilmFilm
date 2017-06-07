package ru.xmn.filmfilmfilm.services.tmdb

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.xmn.filmfilmfilm.services.tmdb.TmdbCredits
import ru.xmn.filmfilmfilm.services.tmdb.TmdbMovieInfo

interface TmdbService {

    @GET("/3/movie/{movie_id}")
    fun getMovieInfo(@Path("movie_id") imdbId: String, @Query("language") language: String = "ru-RU")
            : Observable<TmdbMovieInfo>

    @GET("/3/movie/{movie_id}/credits")
    fun getMovieCredits(@Path("movie_id") imdbId: String)
            : Observable<TmdbCredits>

    @GET("/3/discover/movie")
    fun getMoviesForPerson(@Query("with_people") personId: String, @Query("language") language: String = "ru-RU"): Observable<TmdbDiscoverResponse>

    @GET("/3/discover/movie")
    fun getMoviesForCast(@Query("with_cast") personId: String, @Query("language") language: String = "ru-RU"): Observable<TmdbDiscoverResponse>

    @GET("/3/discover/movie")
    fun getMoviesForCrew(@Query("with_crew") personId: String, @Query("language") language: String = "ru-RU"): Observable<TmdbDiscoverResponse>
}
