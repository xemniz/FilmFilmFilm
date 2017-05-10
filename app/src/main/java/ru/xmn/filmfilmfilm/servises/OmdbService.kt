package ru.xmn.filmfilmfilm.servises

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.xmn.filmfilmfilm.servises.data.OmdbResponse

interface OmdbService {
    @GET()
    fun getMovieShowings(
            @Query("i") imdbId: String)
            : Observable<OmdbResponse>

//    http://www.omdbapi.com/?i=tt1172570
}
