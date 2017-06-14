package ru.xmn.filmfilmfilm.screens.main.films.viewmodels

import android.app.Application
import android.arch.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.OrderedRealmCollection
import io.realm.Realm
import ru.xmn.filmfilmfilm.application.App
import ru.xmn.filmfilmfilm.screens.main.MainActivityModule
import ru.xmn.filmfilmfilm.screens.main.films.FilmsProvider
import ru.xmn.filmfilmfilm.screens.main.films.di.FilmsModule
import ru.xmn.filmfilmfilm.services.film.FilmData
import javax.inject.Inject

class FilmsFragmentViewModel(application: Application?, val daysOffset: Int) : AndroidViewModel(application) {
    val filmIds = MutableLiveData<List<String?>>()
    val films = MediatorLiveData<OrderedRealmCollection<FilmData>>()
    init {
        films.addSource(filmIds) {
            films.value = Realm.getDefaultInstance().where(FilmData::class.java)
                    .`in`("imdbId", filmIds.value?.filter { it!=null }?.toTypedArray())
                    .findAllAsync()
        }
    }

    @Inject
    lateinit var filmsProvider: FilmsProvider

    init {
        App.component.plus(MainActivityModule()).plus(FilmsModule()).inject(this)
        loadMovies()
    }

    fun loadMovies() {
        filmsProvider.getMovies(daysOffset)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ filmIds.value = it }, { it.printStackTrace() })
    }

    class Factory(val application: Application?, val daysOffset: Int) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FilmsFragmentViewModel(application, daysOffset) as T
    }
}