package ru.xmn.filmfilmfilm.screens.persondetails

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.OrderedRealmCollection
import io.realm.Realm
import ru.xmn.filmfilmfilm.application.App
import ru.xmn.filmfilmfilm.common.realmext.query
import ru.xmn.filmfilmfilm.common.realmext.queryAll
import ru.xmn.filmfilmfilm.services.film.FilmData
import ru.xmn.filmfilmfilm.services.tmdb.PersonType
import javax.inject.Inject

class PersonDetailsViewModel(val application: Application, val personId: String, val personType: PersonType) : ViewModel() {
    val films = MutableLiveData<OrderedRealmCollection<FilmData>>().apply { value = Realm.getDefaultInstance().where(FilmData::class.java).equalTo("persons.id", personId).findAll() }

    @Inject
    lateinit var filmsProvider: PersonDetailsProvider

    init {
        App.component.plus(PersonDetailsModule()).inject(this)
        loadMovies()
    }

    fun loadMovies() {
        filmsProvider.getMoviesForPerson(personType, personId)
                .subscribeOn(Schedulers.io())
                .subscribe({ }, { it.printStackTrace() })
    }

    class Factory(val application: Application, val personId: String, val personType: PersonType) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
            return PersonDetailsViewModel(application, personId, personType) as T
        }
    }
}

@Module
class PersonDetailsModule {

}
