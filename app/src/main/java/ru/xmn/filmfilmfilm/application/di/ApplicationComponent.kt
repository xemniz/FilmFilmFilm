package ru.xmn.filmfilmfilm.application.di

import com.zhuinden.servicetree.ServiceTree
import dagger.Component
import ru.xmn.filmfilmfilm.screens.main.MainActivityComponent
import ru.xmn.filmfilmfilm.screens.main.MainActivityModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, DataModule::class))
interface ApplicationComponent {
    fun serviceTree(): ServiceTree
    fun plus(mainActivityModule: MainActivityModule): MainActivityComponent
}