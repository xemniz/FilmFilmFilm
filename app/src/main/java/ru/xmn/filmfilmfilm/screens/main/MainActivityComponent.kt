package ru.xmn.filmfilmfilm.screens.main

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(
        MainActivityModule::class
))
interface MainActivityComponent{
    fun inject(mainActivity: MainActivity)
}