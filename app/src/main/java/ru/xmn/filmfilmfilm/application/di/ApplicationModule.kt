package ru.xmn.filmfilmfilm.application.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.xmn.filmfilmfilm.application.App
import javax.inject.Singleton


@Module
class ApplicationModule (private val app: App){
    @Provides @Singleton
    fun provideApplicationContext(): Context = app
}
