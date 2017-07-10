package ru.xmn.filmfilmfilm.common.extensions

import android.support.annotation.LayoutRes
import android.support.v4.view.ViewCompat
import android.transition.Transition
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
import java.lang.reflect.Type

fun View.pairSharedTransition(): android.support.v4.util.Pair<View, String> {
    return android.support.v4.util.Pair<View, String>(this, ViewCompat.getTransitionName(this))
}

val ViewGroup.views: List<View>
    get() = (0..getChildCount() - 1).map { getChildAt(it) }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Transition.delay(i: Long): Transition {
    this.startDelay = i; return this
}

fun Transition.dur(i: Long): Transition {
    this.duration = i; return this
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context)
            .load(url)
            .into(this)
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
