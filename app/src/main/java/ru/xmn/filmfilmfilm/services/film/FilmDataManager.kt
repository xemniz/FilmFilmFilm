package ru.xmn.filmfilmfilm.services.film

import io.reactivex.schedulers.Schedulers
import io.realm.RealmList
import khronos.Dates
import ru.xmn.filmfilmfilm.common.realmext.queryFirst
import ru.xmn.filmfilmfilm.common.realmext.save
import ru.xmn.filmfilmfilm.common.timeStamp
import ru.xmn.filmfilmfilm.services.imdbId
import ru.xmn.filmfilmfilm.services.kudago.KudaGoManager
import ru.xmn.filmfilmfilm.services.kudago.Movie
import ru.xmn.filmfilmfilm.services.omdb.OmdbManager
import ru.xmn.filmfilmfilm.services.omdb.OmdbResponse
import ru.xmn.filmfilmfilm.services.tmdb.TmdbManager
import ru.xmn.filmfilmfilm.services.tmdb.TmdbMovieInfo
import ru.xmn.filmfilmfilm.services.tmdb.pathToUrl

class FilmDataManager(val tmdb: TmdbManager, val omdb: OmdbManager, val kudaGo: KudaGoManager) {
    fun updateFilmData(movie: Movie) {
        val filmData = movie.imdbId?.let { retrieveOrCreateFilmData(it) } ?: return

        filmData.apply {
            imdbId = movie.imdbId
            title = movie.title
            image = movie.poster.image
            director = movie.director
            genres = movie.genres.fold(RealmList<GenreData>(), {
                list, genre ->
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

        movie.imdbId?.let { updateRatings(filmData) }
    }

    fun updateFilmData(movie: OmdbResponse?) {
        val filmData = movie?.imdbID?.let { retrieveOrCreateFilmData(it) } ?: return

        filmData.apply {
            imdbId = movie.imdbID
            ratings = movie.Ratings.fold(RealmList<RatingData>(), {
                list, rating ->
                list.add(RatingData().apply { source = rating.Source; value = rating.Value }); list
            })
        }.save()
    }

    private fun updateRatings(filmData: FilmData) {
        if (filmData.imdbId == null) return

        omdb.getOmdbInfo(filmData.imdbId!!)
                .subscribeOn(Schedulers.io())
                .subscribe({ updateFilmData(it) })
    }

    fun updateFilmData(movie: TmdbMovieInfo) {
        fun upd(info: TmdbMovieInfo) {
            val filmData = movie.id.let { retrieveOrCreateFilmData(info.imdb_id!!) }

            filmData.apply {
                imdbId = info.imdb_id
                tmdbId = info.id.toString()
                title = info.title
                image = info.poster_path.pathToUrl()
                genres = info.genres.fold(RealmList<GenreData>(), {
                    list, genre ->
                    list.add(GenreData().apply {
                        name = genre.name
                    })
                    list
                })
                val sourceData = sources.find { it.name == SourceData.kudago }
                if (sourceData != null) sourceData.timestamp = Dates.now.timeStamp()
                else sources.add(SourceData().apply {
                    name = SourceData.tmdb
                    timestamp = Dates.now.timeStamp()
                })
            }.save()

            updateRatings(filmData)
        }

        if (movie.imdb_id == null)
            tmdb.getTmdbMovieInfo(movie.id.toString()).subscribeOn(Schedulers.io()).subscribe({ upd(it) })
        else upd(movie)
    }

    private fun retrieveOrCreateFilmData(id: String) = FilmData().queryFirst { query -> query.equalTo("imdbId", id) } ?: FilmData()
}
