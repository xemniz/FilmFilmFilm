package api.kudago

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class HttpClient {
    fun get(url: String): Retrofit {
        val builder = OkHttpClient.Builder()
                .addInterceptor({
                    chain ->
                    println(chain.request().url().toString())
                    chain.proceed(chain.request())
                })

        return Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}
