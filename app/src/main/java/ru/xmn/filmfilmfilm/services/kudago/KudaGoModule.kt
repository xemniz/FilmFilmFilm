package ru.xmn.filmfilmfilm.services.kudago

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.xmn.filmfilmfilm.application.di.provideRestAdapter
import ru.xmn.filmfilmfilm.services.kudago.KudaGoManager
import ru.xmn.filmfilmfilm.services.kudago.KudaGoService
import javax.inject.Named
import javax.inject.Singleton

@Module
class KudaGoModule {
    @Provides @Singleton @Named("kudago")
    fun provideRestAdapterKudago(client: OkHttpClient): Retrofit
            = provideRestAdapter(client, "https://kudago.com/public-api/v1.3/")

    @Provides @Singleton
    fun providesKudaGoService(@Named("kudago") retrofit: Retrofit): KudaGoService
            = retrofit.create(KudaGoService::class.java)

    @Provides @Singleton
    fun providesKudaGoManager(service: KudaGoService): KudaGoManager
            = KudaGoManager(service)
}