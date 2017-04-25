package ru.xmn.filmfilmfilm.screens.main

import dagger.Subcomponent
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.OneDayFilmsComponent
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.OneDayFilmsModule

@Subcomponent(modules = arrayOf(
        MainActivityModule::class
))
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
    fun plus(oneDayFilmsModule: OneDayFilmsModule): OneDayFilmsComponent
}