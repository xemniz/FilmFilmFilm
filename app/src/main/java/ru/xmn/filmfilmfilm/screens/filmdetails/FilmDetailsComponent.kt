package ru.xmn.filmfilmfilm.screens.filmdetails

import dagger.Subcomponent
import ru.xmn.filmfilmfilm.application.di.scopes.ActivityScope

@ActivityScope
@Subcomponent(modules = arrayOf(FilmDetailsModule::class))
interface FilmDetailsComponent {
    fun inject(target: FilmDetailsViewModel)
}