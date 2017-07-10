package ru.xmn.filmfilmfilm.screens.main.films.viewmodels

import android.app.Application
import android.arch.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mu.KLogging
import ru.xmn.filmfilmfilm.application.App
import ru.xmn.filmfilmfilm.common.extensions.logError
import ru.xmn.filmfilmfilm.screens.main.MainActivityModule
import ru.xmn.filmfilmfilm.screens.main.films.FilmsProvider
import ru.xmn.filmfilmfilm.screens.main.films.di.FilmsModule
import ru.xmn.filmfilmfilm.services.film.FilmData
import javax.inject.Inject

class FilmsFragmentViewModel(application: Application?, val daysOffset: Int) : AndroidViewModel(application) {

    companion object: KLogging()

    @Inject
    lateinit var filmsProvider: FilmsProvider

    val filmIds = MutableLiveData<List<String?>>()
    val films = MediatorLiveData<List<FilmData>>()
    init {
        films.addSource(filmIds) {
            val ids = filmIds.value
            filmsProvider.subscribeToFilms(ids).observeOn(AndroidSchedulers.mainThread()).subscribe({ films.value = it })
        }
        App.component.plus(MainActivityModule()).plus(FilmsModule()).inject(this)
        loadMovies()
    }



    fun loadMovies() {
        filmsProvider.getMovies(daysOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ filmIds.value = it }, { logError(logger, "filmsProvider.getMovies(daysOffset)", it) })
    }

    class Factory(val application: Application?, val daysOffset: Int) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FilmsFragmentViewModel(application, daysOffset) as T
    }
}