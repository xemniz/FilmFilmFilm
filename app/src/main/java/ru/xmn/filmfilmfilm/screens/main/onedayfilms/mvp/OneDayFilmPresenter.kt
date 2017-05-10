package ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.xmn.filmfilmfilm.application.di.scopes.FragmentScope
import ru.xmn.filmfilmfilm.common.ui.BasePresenter
import javax.inject.Inject

/**
 * Created by xmn on 30.04.2017.
 */

@FragmentScope
class OneDayFilmPresenter @Inject
constructor(val oneDayFilmProvider: OneDayFilmProvider)
    : BasePresenter<OneDayFilmView>() {

    fun loadMovies() {
        oneDayFilmProvider.getMovies().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.showMovies(it)
                })
    }
}


