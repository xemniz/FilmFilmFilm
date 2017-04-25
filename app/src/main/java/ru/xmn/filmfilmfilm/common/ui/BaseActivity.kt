package ru.xmn.filmfilmfilm.common.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.xmn.filmfilmfilm.application.App
import ru.xmn.filmfilmfilm.application.di.ApplicationComponent

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        val IMAGE_TRANSITION_NAME = "activity_image_transition"
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies(App.component)
    }

    abstract fun injectDependencies(applicationComponent: ApplicationComponent)
}