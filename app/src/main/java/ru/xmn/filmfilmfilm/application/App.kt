package ru.xmn.filmfilmfilm.application

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import ru.xmn.filmfilmfilm.BuildConfig
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
        initializeRealm()
    }

    private fun initializeRealm() {
        Realm.init(this)

        var configBuilder: RealmConfiguration.Builder = RealmConfiguration.Builder()
                .schemaVersion(1)

        // IMPORTANT:
        // While we're developing, any change in Realm will automatically reset the database.
        // Be careful!
        if (BuildConfig.DEBUG)
            configBuilder = configBuilder.deleteRealmIfMigrationNeeded()

        val config = configBuilder
//                .migration(Migration())
                .build()

        Realm.setDefaultConfiguration(config)
    }

    fun initializeDagger() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}
