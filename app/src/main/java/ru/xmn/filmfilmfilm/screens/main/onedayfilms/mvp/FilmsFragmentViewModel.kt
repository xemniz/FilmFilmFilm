package ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp

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
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.di.OneDayFilmsModule
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.viewmodels.FilmItemViewModel
import javax.inject.Inject

class FilmsFragmentViewModel(application: Application?, val daysOffset: Int) : AndroidViewModel(application) {
    val films = MutableLiveData<List<FilmItemViewModel>>()

    @Inject
    lateinit var oneDayFilmProvider: OneDayFilmProvider

    init {
        App.component.plus(MainActivityModule()).plus(OneDayFilmsModule()).inject(this)
        loadMovies()
    }

    fun loadMovies() {
        oneDayFilmProvider.getMovies(daysOffset)!!.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { films.value = it }, Consumer { it.printStackTrace() })
    }

    class Factory(val application: Application?, val daysOffset: Int) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FilmsFragmentViewModel(application, daysOffset) as T
    }
}