package ru.xmn.filmfilmfilm.kudago

import ru.xmn.filmfilmfilm.kudago.data.KudaGoMoviesResponse
import ru.xmn.filmfilmfilm.kudago.data.KudaGoShowingsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface KudaGoService {
    @GET("movie-showings/")
    fun getMovieShowings(
            @Query("location") location: String,
            @Query("expand") expand: String,
            @Query("page_size") pageSize: Int,
            @Query("actual_since") actualSince: Long,
            @Query("actual_until") actualUntil: Long)
            : Observable<KudaGoShowingsResponse>

    @GET("movies/")
    fun getMovies(
            @Query("location") location: String,
            @Query("expand") expand: String,
            @Query("page_size") pageSize: Int,
            @Query("actual_since") actualSince: Long,
            @Query("actual_until") actualUntil: Long)
            : Observable<KudaGoMoviesResponse>
}
