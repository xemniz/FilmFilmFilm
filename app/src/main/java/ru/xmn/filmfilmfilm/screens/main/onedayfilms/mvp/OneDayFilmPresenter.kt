package ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.xmn.filmfilmfilm.application.di.scopes.FragmentScope
import ru.xmn.filmfilmfilm.common.ui.BasePresenter
import ru.xmn.filmfilmfilm.kudago.KudaGoService
import ru.xmn.filmfilmfilm.kudago.data.KudaGoMoviesResponse
import ru.xmn.filmfilmfilm.kudago.data.toViewModel
import java.util.*
import javax.inject.Inject

/**
 * Created by xmn on 30.04.2017.
 */

@FragmentScope
class OneDayFilmPresenter @Inject
constructor(val kudaGo: KudaGoService)
    : BasePresenter<OneDayFilmView>() {

    fun loadMovies() {
        getMovies().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ view?.showMovies(it.results.map { it.toViewModel() }) })
    }

    fun getMovies(): Observable<KudaGoMoviesResponse> {
        val since = Date().time / 1000
        val until = since + 3600 * 24
        return kudaGo.getMovies(actualSince = since, actualUntil = until)
    }
}

