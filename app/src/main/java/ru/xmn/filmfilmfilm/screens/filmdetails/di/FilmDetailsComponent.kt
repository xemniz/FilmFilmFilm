package ru.xmn.filmfilmfilm.screens.filmdetails.di

import dagger.Subcomponent
import ru.xmn.filmfilmfilm.application.di.scopes.ActivityScope
import ru.xmn.filmfilmfilm.screens.filmdetails.FilmDetailsViewModel

@ActivityScope
@Subcomponent(modules = arrayOf(FilmDetailsModule::class))
interface FilmDetailsComponent {
    fun inject(target: FilmDetailsViewModel)
}