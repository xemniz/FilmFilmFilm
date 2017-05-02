package ru.xmn.filmfilmfilm.common

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.moshi.Moshi

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ImageView.loadUrl(url: String) {
//    Picasso.with(context).load(url).into(this)
}

inline fun <reified T> Moshi.fromJson(json: String?): T? {
    val jsonAdapter = this.adapter(T::class.java)
    return jsonAdapter.fromJson(json)
}