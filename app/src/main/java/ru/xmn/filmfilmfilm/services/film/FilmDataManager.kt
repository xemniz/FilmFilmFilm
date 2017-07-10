package ru.xmn.filmfilmfilm.services.film

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmList
import khronos.Dates
import mu.KLogging
import ru.xmn.filmfilmfilm.common.extensions.log
import ru.xmn.filmfilmfilm.common.extensions.logError
import ru.xmn.filmfilmfilm.common.extensions.timeStamp
import ru.xmn.filmfilmfilm.common.realmext.queryFirst
import ru.xmn.filmfilmfilm.common.realmext.save
import ru.xmn.filmfilmfilm.services.imdbId
import ru.xmn.filmfilmfilm.services.kudago.KudaGoManager
import ru.xmn.filmfilmfilm.services.kudago.Movie
import ru.xmn.filmfilmfilm.services.omdb.OmdbManager
import ru.xmn.filmfilmfilm.services.omdb.OmdbResponse
import ru.xmn.filmfilmfilm.services.tmdb.*
import java.util.concurrent.TimeUnit

class FilmDataManager(val tmdb: TmdbManager, val omdb: OmdbManager, val kudaGo: KudaGoManager) {
    //todo добавить проверки на время последнего обновления везде.

    companion object : KLogging()

    //region updateFilmData
    fun updateFilmData(movie: Movie): FilmData? {
        val filmData = movie.imdbId?.let { retrieveOrCreateFilmData(it) } ?: return null
        if (!isSourceOutdated(filmData.sources, SourceData.kudago, SourceData.kudagoPeriod)) return null

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
            trailer = movie.trailer
            addSource(sources, SourceData.kudago)
        }.save()

        return filmData
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

    fun updateFilmData(movie: TmdbMovieInfo): FilmData? {
        fun upd(info: TmdbMovieInfo): FilmData {
            val filmData = movie.id.let { retrieveOrCreateFilmData(info.imdb_id!!) }
            if (!isSourceOutdated(filmData.sources, SourceData.tmdb, SourceData.tmdbPeriod)) return filmData

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

            return filmData
        }

        if (movie.imdb_id == null)
            return tmdb.getTmdbMovieInfo(movie.id.toString()).subscribeOn(Schedulers.io()).map { upd(it) }.blockingFirst(null)
        else return upd(movie)
    }

    fun updateFilmData(it: TmdbCredits, imdbId: String) {
        val filmData = retrieveOrCreateFilmData(imdbId)
        filmData.apply { persons = it.toRealm() }.save()
    }
    //endregion

    //region queryToUpdateFilmData
    fun updateFromTmdb(filmData: FilmData) {
        if (filmData.backdrop == null)
            filmData.imdbId?.let {
                tmdb.getTmdbMovieInfo(it).subscribeOn(Schedulers.io()).subscribe({ updateFilmData(it) }, { logError(logger, "updateFromTmdb(filmData: FilmData)", it) })
            }
    }

    fun updateTrailer(filmData: FilmData) {
    }

    fun updateRatings(filmData: FilmData) {
        if (filmData.imdbId == null) return

        omdb.getOmdbInfo(filmData.imdbId!!)
                .subscribeOn(Schedulers.io())
                .subscribe({ updateFilmData(it) }, { logError(logger, "updateRatings(filmData: FilmData)", it) })
    }

    fun updateCredits(filmData: FilmData) {
        if (filmData.imdbId == null || filmData.persons.size > 0) return

        val imdbId = filmData.imdbId!!
        tmdb.getTmdbCredits(imdbId)
                .subscribeOn(Schedulers.io())
                .subscribe({ updateFilmData(it, imdbId) }, { logError(logger, "updateCredits(filmData: FilmData)", it) })

    }
    //endregion

    fun subscribeToFilms(fieldName: String, ids: List<String?>?) =
            Observable.create(object : ObservableOnSubscribe<List<FilmData>> {
                override fun subscribe(e: ObservableEmitter<List<FilmData>>) {
                    val defaultInstance = Realm.getDefaultInstance()
                    defaultInstance.where(FilmData::class.java)
                            .`in`(fieldName, ids?.filter { it != null }?.toTypedArray() ?: emptyArray())
                            .findAllAsync()
                            .addChangeListener { collection, changeSet -> e.onNext(defaultInstance.copyFromRealm(collection)) }
                    e.setCancellable { defaultInstance.close(); log(logger, "fun subscribeToFilms", "on cancel") }
                }
            })
                    .debounce(500, TimeUnit.MILLISECONDS)

    //region util
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
    //endregion
}
