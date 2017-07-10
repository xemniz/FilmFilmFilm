package ru.xmn.filmfilmfilm.services.omdb

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.xmn.filmfilmfilm.application.Config
import ru.xmn.filmfilmfilm.application.di.provideRestAdapter
import ru.xmn.filmfilmfilm.common.extensions.addParameterInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
class OmdbModule {
    @Provides @Singleton @Named("omdb")
    fun provideRestAdapterOmdb(client: OkHttpClient): Retrofit
            = provideRestAdapter(client.addParameterInterceptor("apikey", Config.OMDB_API_KEY), "http://www.omdbapi.com/")


    @Provides @Singleton
    fun providesOmdbService(@Named("omdb") retrofit: Retrofit): OmdbService
            = retrofit.create(OmdbService::class.java)

    @Provides @Singleton
    fun providesOmdbManager(service: OmdbService): OmdbManager = OmdbManager(service)
}