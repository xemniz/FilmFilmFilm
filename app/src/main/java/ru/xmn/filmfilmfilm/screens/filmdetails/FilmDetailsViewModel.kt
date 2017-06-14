package ru.xmn.filmfilmfilm.screens.filmdetails

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import ru.xmn.filmfilmfilm.application.App
import ru.xmn.filmfilmfilm.screens.filmdetails.di.FilmDetailsModule
import ru.xmn.filmfilmfilm.services.film.FilmData
import ru.xmn.filmfilmfilm.services.omdb.OmdbFilm
import ru.xmn.filmfilmfilm.services.omdb.OmdbResponse
import ru.xmn.filmfilmfilm.services.tmdb.TmdbCredits
import ru.xmn.filmfilmfilm.services.tmdb.TmdbMovieInfo
import javax.inject.Inject

class FilmDetailsViewModel(application: Application?, val imdbId: String) : ViewModel() {
    @Inject
    lateinit var provider: FilmDetailsProvider

    val filminfo = MutableLiveData<FilmData>()
    init {
        Realm.getDefaultInstance()
                .where(FilmData::class.java)
                .equalTo("imdbId", imdbId)
                .findFirstAsync()
                .addChangeListener <FilmData>{ film, _ -> filminfo.value = film }
    }

    init {
        App.component.plus(FilmDetailsModule()).inject(this)
    }

    class Factory(val application: Application?, val imdbId: String) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FilmDetailsViewModel(application, imdbId) as T
    }
}

