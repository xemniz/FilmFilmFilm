package ru.xmn.filmfilmfilm.services.kudago

import io.reactivex.Observable
import khronos.*
import ru.xmn.filmfilmfilm.common.extensions.timeStamp

class KudaGoManager(private val service: KudaGoService){

    fun getMovies(addDays: Int = 0): Observable<KudaGoMoviesResponse> {
        val since = (Dates.now + addDays.days).beginningOfDay
        val until = since.endOfDay
        return service
                .getMovies(actualSince = since.timeStamp(), actualUntil = until.timeStamp())
    }

    fun getMovies(imdbId: String): Observable<KudaGoMoviesResponse> {
        return service
                .getMovies(imdbUrl = imdbId.toImdbUrl())
    }

}

private fun String.toImdbUrl(): String {
    return "http://www.imdb.com/title/$this/"
}
