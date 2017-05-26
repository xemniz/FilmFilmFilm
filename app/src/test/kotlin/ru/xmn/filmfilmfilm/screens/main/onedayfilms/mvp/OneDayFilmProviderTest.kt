package ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp

import android.app.Application
import io.reactivex.functions.Consumer
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import ru.xmn.filmfilmfilm.application.di.DataModule

/**
 * Created by USER on 26.05.2017.
 */
class OneDayFilmProviderTest {
    @Test
    fun getOmdb() {
        val dataModule = DataModule()
        dataModule.providesOmdbService(dataModule.provideRestAdapterOmdb(dataModule.provideOkHttpClient())).getMovieInfo("tt4287320").subscribe(Consumer {
            println(it)
            Assert.assertEquals("tt4287320", it.imdbID)
        })
    }

}