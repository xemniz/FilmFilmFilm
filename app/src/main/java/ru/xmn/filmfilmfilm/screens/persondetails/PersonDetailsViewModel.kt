package ru.xmn.filmfilmfilm.screens.persondetails

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.xmn.filmfilmfilm.application.App
import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmItemViewData
import ru.xmn.filmfilmfilm.services.tmdb.PersonType
import javax.inject.Inject

class PersonDetailsViewModel(val application: Application, val personId: String, val personType: PersonType) : ViewModel() {
    val films = MutableLiveData<List<FilmItemViewData>>()

    @Inject
    lateinit var filmsProvider: PersonDetailsProvider

    init {
        App.component.plus(PersonDetailsModule()).inject(this)
        loadMovies()
    }

    fun loadMovies() {
        filmsProvider.getMoviesForPerson(personType, personId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ films.value = it }, { it.printStackTrace() })
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
