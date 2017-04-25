package ru.xmn.filmfilmfilm.screens.main.onedayfilms

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(
        OneDayFilmsModule::class
))
interface OneDayFilmsComponent {
    fun inject(mainActivity: OneDayFilmsFragment)
}