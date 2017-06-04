package ru.xmn.filmfilmfilm.services.omdb

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.xmn.filmfilmfilm.services.data.OmdbResponse

interface TmdbService {

    @GET("/")
    fun getMovieInfo(
            @Query("i") imdbId: String)
            : Observable<OmdbResponse>
}
