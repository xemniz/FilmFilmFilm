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
import ru.xmn.filmfilmfilm.kudago.KudaGoService
import javax.inject.Singleton

@Module
class DataModule {

    @Provides @Singleton
    fun provideCache(context: Context) = Cache(context.cacheDir, 10 * 1024 * 1024.toLong())

    @Provides @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient =
            OkHttpClient().newBuilder()
                    .cache(cache)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
                    })
                    .build()

    @Provides @Singleton
    fun provideRestAdapter(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://kudago.com/public-api/v1.3/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides @Singleton
    fun providesKudaGoService(retrofit: Retrofit): KudaGoService = retrofit.create(KudaGoService::class.java)
}