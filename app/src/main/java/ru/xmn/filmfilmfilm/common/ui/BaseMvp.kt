package ru.xmn.filmfilmfilm.common.ui

import android.content.Context
import android.support.annotation.StringRes

open class BasePresenter<V : BaseView> {
    protected var view: V? = null

    fun attachView(view: V) {
        this.view = view
    }

    fun detachView() {
        view = null
    }
}

interface BaseView {

    fun getContext(): Context

    fun showError(error: String?)

    fun showError(@StringRes stringResId: Int)

    fun showMessage(@StringRes srtResId: Int)

    fun showMessage(message: String)

}
