package ru.xmn.filmfilmfilm.application

import android.app.Application
import ru.xmn.filmfilmfilm.application.di.ApplicationComponent
import ru.xmn.filmfilmfilm.application.di.ApplicationModule
import ru.xmn.filmfilmfilm.application.di.DaggerApplicationComponent

class App : Application() {

    companion object {
        lateinit var component: ApplicationComponent
        val TAG: String = "App"
        val DAGGER_COMPONENT: String = "AppComponent"
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    fun initializeDagger() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()

        component.serviceTree().createRootNode(TAG).bindService(DAGGER_COMPONENT, component)
    }
}
