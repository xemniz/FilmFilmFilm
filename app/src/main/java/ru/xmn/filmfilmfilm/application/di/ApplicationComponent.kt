package ru.xmn.filmfilmfilm.application.di

import com.zhuinden.servicetree.ServiceTree
import dagger.Component
import ru.xmn.filmfilmfilm.screens.main.MainActivityComponent
import ru.xmn.filmfilmfilm.screens.main.MainActivityModule

@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun serviceTree(): ServiceTree
    fun plus(mainActivityModule: MainActivityModule): MainActivityComponent
}