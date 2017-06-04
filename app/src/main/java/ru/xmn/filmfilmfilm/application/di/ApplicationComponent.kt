package ru.xmn.filmfilmfilm.application.di

import com.zhuinden.servicetree.ServiceTree
import dagger.Component
import ru.xmn.filmfilmfilm.screens.main.MainActivityComponent
import ru.xmn.filmfilmfilm.screens.main.MainActivityModule
import ru.xmn.filmfilmfilm.services.kudago.KudaGoModule
import ru.xmn.filmfilmfilm.services.omdb.OmdbModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        NetworkModule::class,
        OmdbModule::class,
        KudaGoModule::class
))
interface ApplicationComponent {
    fun serviceTree(): ServiceTree
    fun plus(mainActivityModule: MainActivityModule): MainActivityComponent
}