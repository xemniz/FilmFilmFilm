package ru.xmn.filmfilmfilm.screens.main.onedayfilms.di

import dagger.Subcomponent
import ru.xmn.filmfilmfilm.application.di.scopes.FragmentScope
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp.FilmsFragmentViewModel
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp.OneDayFilmsFragment

@FragmentScope
@Subcomponent(modules = arrayOf(
        OneDayFilmsModule::class
))
interface OneDayFilmsComponent {
    fun inject(fragment: OneDayFilmsFragment)
    fun inject(fragment: FilmsFragmentViewModel)
}