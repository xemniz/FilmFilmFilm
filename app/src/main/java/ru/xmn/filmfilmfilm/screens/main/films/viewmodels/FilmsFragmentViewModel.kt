package ru.xmn.filmfilmfilm.screens.main.films.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import ru.xmn.filmfilmfilm.application.App
import ru.xmn.filmfilmfilm.screens.main.MainActivityModule
import ru.xmn.filmfilmfilm.screens.main.films.FilmsProvider
import ru.xmn.filmfilmfilm.screens.main.films.di.FilmsModule
import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmItemViewModel
import javax.inject.Inject

class FilmsFragmentViewModel(application: Application?, val daysOffset: Int) : AndroidViewModel(application) {
    val films = MutableLiveData<List<FilmItemViewModel>>()

    @Inject
    lateinit var filmsProvider: FilmsProvider

    init {
        App.component.plus(MainActivityModule()).plus(FilmsModule()).inject(this)
        loadMovies()
    }

    fun loadMovies() {
        filmsProvider.getMovies(daysOffset)!!.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { films.value = it }, Consumer { it.printStackTrace() })
    }

    class Factory(val application: Application?, val daysOffset: Int) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FilmsFragmentViewModel(application, daysOffset) as T
    }
}