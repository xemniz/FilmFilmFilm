package ru.xmn.filmfilmfilm.common

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.xmn.filmfilmfilm.BuildConfig
import java.util.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import com.squareup.moshi.Types.newParameterizedType
import java.lang.reflect.Type


fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url).into(this)
}

fun Date.timeStamp(): Long = this.time / 1000

inline fun <reified T> Moshi.fromJson(json: String?): T? {
    val jsonAdapter = this.adapter(T::class.java)
    return jsonAdapter.fromJson(json)
}

inline fun <reified T> Moshi.listFromJson(json: String?): List<T> {
    val listMyData: Type = Types.newParameterizedType(List::class.java, T::class.java)
    val adapter: JsonAdapter<List<T>> = this.adapter(listMyData)
    return adapter.fromJson(json)
}

inline fun <reified T> T.toJson(): String {
    val jsonAdapter = Moshi.Builder().build().adapter(T::class.java)
    return jsonAdapter.toJson(this)
}

fun OkHttpClient.addParameterInterceptor(key: String, value: String): OkHttpClient {
    return this.newBuilder()
            .addInterceptor {
                val url = it.request().url().newBuilder().addQueryParameter(key, value).build()
                val request = it.request().newBuilder().url(url).build()
                it.proceed(request)
            }
            .build()
}

fun OkHttpClient.addLoggingInterceptor(): OkHttpClient {
    return this.newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .build()
}

fun Float.abs(): Float = Math.abs(this)
