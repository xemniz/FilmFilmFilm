package ru.xmn.filmfilmfilm.application

import android.app.Application
import ru.xmn.filmfilmfilm.application.di.ApplicationComponent
import ru.xmn.filmfilmfilm.application.di.ApplicationModule
import ru.xmn.filmfilmfilm.application.di.DaggerApplicationComponent

class App : Application() {

    companion object {
        lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    fun initializeDagger() {
        graph = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}
