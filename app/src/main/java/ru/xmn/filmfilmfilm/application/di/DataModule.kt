package ru.xmn.filmfilmfilm.application.di

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.xmn.filmfilmfilm.BuildConfig
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.servises.KudaGoService
import ru.xmn.filmfilmfilm.servises.OmdbService
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule {

    @Provides @Singleton
    fun provideCache(context: Context)
            = Cache(context.cacheDir, 10 * 1024 * 1024.toLong())

    @Provides @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient
            = OkHttpClient().newBuilder()
            .cache(cache)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
            })
            .build()

    @Provides @Singleton @Named("kudago")
    fun provideRestAdapter0(client: OkHttpClient): Retrofit
            = provideRestAdapter(client, "https://kudago.com/public-api/v1.3/")

    @Provides @Singleton @Named("omdb")
    fun provideRestAdapter1(client: OkHttpClient): Retrofit
            = provideRestAdapter(client, "http://www.omdbapi.com/")

    fun provideRestAdapter(client: OkHttpClient, url: String): Retrofit
            = Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides @Singleton
    fun providesKudaGoService(@Named("kudago") retrofit: Retrofit): KudaGoService
            = retrofit.create(KudaGoService::class.java)

    @Provides @Singleton
    fun providesOmdbService(@Named("kudago") retrofit: Retrofit): OmdbService
            = retrofit.create(OmdbService::class.java)
}