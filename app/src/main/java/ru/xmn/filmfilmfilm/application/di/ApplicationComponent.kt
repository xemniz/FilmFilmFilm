package ru.xmn.filmfilmfilm.application.di

import dagger.Component
import ru.xmn.filmfilmfilm.screens.main.MainActivityComponent
import ru.xmn.filmfilmfilm.screens.main.MainActivityModule

@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun plus(mainActivityModule: MainActivityModule): MainActivityComponent
}