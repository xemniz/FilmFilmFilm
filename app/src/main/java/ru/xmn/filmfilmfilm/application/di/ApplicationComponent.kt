package ru.xmn.filmfilmfilm.application.di

import dagger.Component
import ru.xmn.filmfilmfilm.screens.filmdetails.di.FilmDetailsComponent
import ru.xmn.filmfilmfilm.screens.filmdetails.di.FilmDetailsModule
import ru.xmn.filmfilmfilm.screens.main.MainActivityComponent
import ru.xmn.filmfilmfilm.screens.main.MainActivityModule
import ru.xmn.filmfilmfilm.screens.persondetails.PersonDetailsComponent
import ru.xmn.filmfilmfilm.screens.persondetails.PersonDetailsModule
import ru.xmn.filmfilmfilm.services.kudago.KudaGoModule
import ru.xmn.filmfilmfilm.services.omdb.OmdbModule
import ru.xmn.filmfilmfilm.services.omdb.TmdbModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        NetworkModule::class,
        OmdbModule::class,
        TmdbModule::class,
        KudaGoModule::class
))
interface ApplicationComponent {
    fun plus(mainActivityModule: MainActivityModule): MainActivityComponent
    fun plus(mainActivityModule: FilmDetailsModule): FilmDetailsComponent
    fun plus(personDetailsModule: PersonDetailsModule): PersonDetailsComponent
}

