package ru.xmn.filmfilmfilm.screens.persondetails

import dagger.Subcomponent
import ru.xmn.filmfilmfilm.application.di.scopes.ActivityScope

@ActivityScope
@Subcomponent(modules = arrayOf(PersonDetailsModule::class))
interface PersonDetailsComponent {
    fun inject(personDetailsViewModel: PersonDetailsViewModel)
}