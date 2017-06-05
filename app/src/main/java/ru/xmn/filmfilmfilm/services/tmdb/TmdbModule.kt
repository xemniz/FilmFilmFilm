package ru.xmn.filmfilmfilm.services.omdb

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.xmn.filmfilmfilm.application.Config
import ru.xmn.filmfilmfilm.application.di.provideRestAdapter
import ru.xmn.filmfilmfilm.common.addParameterInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
class TmdbModule {
    @Provides @Singleton @Named("tmdb")
    fun provideRestAdapterTmdb(client: OkHttpClient): Retrofit
            = provideRestAdapter(client.addParameterInterceptor("api_key", Config.TMDB_API_KEY), "https://api.themoviedb.org/")


    @Provides @Singleton
    fun providesTmdbService(@Named("tmdb") retrofit: Retrofit): TmdbService
            = retrofit.create(TmdbService::class.java)

    @Provides @Singleton
    fun providesTmdbManager(service: TmdbService): TmdbManager = TmdbManager(service)
}