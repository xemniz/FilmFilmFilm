package ru.xmn.filmfilmfilm.screens.main

import dagger.Subcomponent
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.di.OneDayFilmsComponent
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.di.OneDayFilmsModule

@Subcomponent(modules = arrayOf(
        MainActivityModule::class
))
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
    fun plus(oneDayFilmsModule: OneDayFilmsModule): OneDayFilmsComponent
}