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
import ru.xmn.filmfilmfilm.services.tmdb.*

class FilmDataManager(val tmdb: TmdbManager, val omdb: OmdbManager, val kudaGo: KudaGoManager) {

    fun updateFilmData(movie: Movie) {
        val filmData = movie.imdbId?.let { retrieveOrCreateFilmData(it) } ?: return
        if (!isSourceOutdated(filmData.sources, SourceData.kudago, SourceData.kudagoPeriod)) return

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
            addSource(sources, SourceData.kudago)
        }.save()

        updateRatings(filmData)
        updateCredits(filmData)
    }

    fun updateFilmData(movie: OmdbResponse?) {
        val filmData = movie?.imdbID?.let { retrieveOrCreateFilmData(it) } ?: return
        if (!isSourceOutdated(filmData.sources, SourceData.omdb, SourceData.omdbPeriod)) return

        filmData.apply {
            imdbId = movie.imdbID
            ratings = movie.Ratings.fold(RealmList<RatingData>(), {
                list, rating ->
                list.add(RatingData().apply { source = rating.Source; value = rating.Value }); list
            })
            addSource(sources, SourceData.omdb)
        }.save()
    }

    fun updateFilmData(movie: TmdbMovieInfo) {
        fun upd(info: TmdbMovieInfo) {
            val filmData = movie.id.let { retrieveOrCreateFilmData(info.imdb_id!!) }
            if (!isSourceOutdated(filmData.sources, SourceData.tmdb, SourceData.tmdbPeriod)) return

            filmData.apply {
                imdbId = info.imdb_id
                tmdbId = info.id.toString()
                title = info.title
                image = info.poster_path.pathToUrl()
                overview = info.overview
                backdrop = info.backdrop_path.pathToUrl()
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
                addSource(sources, SourceData.tmdb)
            }.save()

            updateRatings(filmData)
            updateCredits(filmData)
        }

        if (movie.imdb_id == null)
            tmdb.getTmdbMovieInfo(movie.id.toString()).subscribeOn(Schedulers.io()).subscribe({ upd(it) })
        else upd(movie)
    }

    fun updateFilmData(it: TmdbCredits, imdbId: String) {
        val filmData = retrieveOrCreateFilmData(imdbId)
        filmData.apply { persons = it.toRealm() }.save()
    }

    private fun updateRatings(filmData: FilmData) {
        if (filmData.imdbId == null) return

        omdb.getOmdbInfo(filmData.imdbId!!)
                .subscribeOn(Schedulers.io())
                .subscribe({ updateFilmData(it) })
    }

    private fun updateCredits(filmData: FilmData) {
        if (filmData.imdbId == null) return

        val imdbId = filmData.imdbId!!
        tmdb.getTmdbCredits(imdbId)
                .subscribeOn(Schedulers.io())
                .subscribe({ updateFilmData(it, imdbId) }, {})

    }

    private fun retrieveOrCreateFilmData(id: String) = FilmData().queryFirst { query -> query.equalTo("imdbId", id) } ?: FilmData()

    private fun addSource(sources: RealmList<SourceData>, source: String) {
        val sourceData = sources.find { it.name == source }
        if (sourceData != null) sourceData.timestamp = Dates.now.timeStamp()
        else sources.add(SourceData().apply {
            name = SourceData.kudago
            timestamp = Dates.now.timeStamp()
        })
    }

    private fun isSourceOutdated(sources: RealmList<SourceData>, source: String, outdatePeriod: Long): Boolean {
        val sourceData = sources.find { it.name == source }
        return when {
            sourceData == null -> true
            else -> (Dates.now.timeStamp() - (sourceData.timestamp ?: 0)) > outdatePeriod
        }
    }
}
