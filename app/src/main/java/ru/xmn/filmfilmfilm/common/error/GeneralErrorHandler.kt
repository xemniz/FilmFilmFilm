package ru.xmn.filmfilmfilm.common.error

import android.support.annotation.StringRes
import android.text.TextUtils
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import io.reactivex.functions.Consumer
import retrofit2.HttpException
import retrofit2.Response
import ru.xmn.filmfilmfilm.common.extensions.fromJson
import ru.xmn.filmfilmfilm.common.ui.BaseView
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class GeneralErrorHandler(view: BaseView? = null,
                          private val mShowError: Boolean = false,
                          val onFailure: (Throwable, ErrorBody?, Boolean) -> Unit)
    : Consumer<Throwable> {

    private val mViewReference: WeakReference<BaseView>

    init {
        mViewReference = WeakReference<BaseView>(view)
    }

    override fun accept(throwable: Throwable) {
        var isNetwork = false
        var errorBody: ErrorBody? = null
        if (isNetworkError(throwable)) {
            isNetwork = true
            showMessage("R.string.internet_connection_unavailable")
        } else if (throwable is HttpException) {
            errorBody = ErrorBody.parseError(throwable.response())
            if (errorBody != null) {
                handleError(errorBody)
            }
        }

        onFailure(throwable, errorBody, isNetwork)
    }

    private fun isNetworkError(throwable: Throwable): Boolean {
        return throwable is SocketException ||
                throwable is UnknownHostException ||
                throwable is SocketTimeoutException
    }

    private fun handleError(errorBody: ErrorBody) {
        if (errorBody.code != ErrorBody.UNKNOWN_ERROR) {
            showErrorIfRequired("R.string.server_error")
        }
    }

    private fun showErrorIfRequired(@StringRes strResId: Int) {
        if (mShowError) {
            mViewReference.get()?.showError(strResId)
        }
    }

    private fun showErrorIfRequired(error: String) {
        if (mShowError && !TextUtils.isEmpty(error)) {
            mViewReference.get()?.showError(error)
        }
    }

    private fun showMessage(@StringRes strResId: Int) {
        mViewReference.get()?.showMessage(strResId)
    }

    private fun showMessage(error: String) {
        if (error.isNotEmpty()) {
            mViewReference.get()?.showError(error)
        }
    }

}

data class ErrorBody(val code: Int, @Json(name = "error") val message: String) {

    override fun toString(): String {
        return "{code:$code, message:\"$message\"}"
    }

    companion object {

        val UNKNOWN_ERROR = 0

        private val MOSHI = Moshi.Builder().build()

        fun parseError(response: Response<*>?): ErrorBody? {
            return (response?.errorBody())?.let {
                try {
                    MOSHI.fromJson(it.string())
                } catch (ignored: IOException) {
                    null
                }
            }
        }
    }

}

