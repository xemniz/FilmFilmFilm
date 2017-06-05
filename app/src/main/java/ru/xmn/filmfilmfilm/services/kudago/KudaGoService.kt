package ru.xmn.filmfilmfilm.services.kudago

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
            @Query("location") location: String = "msk",
            @Query("expand") expand: String = "movie,place",
            @Query("fields") fields: String = "id,title,poster,description,body_text,genres" +
                    ",original_title,locale,country,year,language,mpaa_rating,age_restriction" +
                    ",stars,director,writer,awards,trailer," +
                    "images,imdb_url,imdb_rating",
            @Query("page_size") pageSize: Int = 100,
            @Query("actual_since") actualSince: Long,
            @Query("actual_until") actualUntil: Long)
            : Observable<KudaGoMoviesResponse>
}
