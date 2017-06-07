package ru.xmn.filmfilmfilm.screens.main.films.di

import dagger.Subcomponent
import ru.xmn.filmfilmfilm.application.di.scopes.FragmentScope
import ru.xmn.filmfilmfilm.screens.PersonDetailsViewModel
import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmsFragmentViewModel

@FragmentScope
@Subcomponent(modules = arrayOf(
        FilmsModule::class
))
interface FilmsComponent {
    fun inject(fragment: FilmsFragmentViewModel)
}