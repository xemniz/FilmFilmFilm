package ru.xmn.filmfilmfilm.screens.persondetails

import android.app.Application
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmResults
import ru.xmn.filmfilmfilm.application.App
import ru.xmn.filmfilmfilm.common.realmext.query
import ru.xmn.filmfilmfilm.common.realmext.queryAll
import ru.xmn.filmfilmfilm.services.film.FilmData
import ru.xmn.filmfilmfilm.services.tmdb.PersonType
import javax.inject.Inject

class PersonDetailsViewModel(val application: Application, val personId: String, val personType: PersonType) : ViewModel() {

    @Inject
    lateinit var filmsProvider: PersonDetailsProvider
    val filmIds = MutableLiveData<List<String?>>()
    val films = MediatorLiveData<List<FilmData>>()
    var subscription: Disposable? = null

    init {
        App.component.plus(PersonDetailsModule()).inject(this)
        loadMovies()
        films.addSource(filmIds) {
            val ids = filmIds.value
            subscription = filmsProvider.subscribeToFilms(ids).observeOn(AndroidSchedulers.mainThread()).subscribe({ films.value = it })
        }
    }

    fun loadMovies() {
        filmsProvider.getMoviesForPerson(personType, personId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ filmIds.value = it }, { it.printStackTrace() })
    }

    class Factory(val application: Application, val personId: String, val personType: PersonType) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
            @Suppress("UNCHECKED_CAST")
            return PersonDetailsViewModel(application, personId, personType) as T
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }
}

@Module
class PersonDetailsModule {

}
