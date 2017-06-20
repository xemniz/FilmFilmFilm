package ru.xmn.filmfilmfilm.screens.filmdetails

import android.app.SharedElementCallback
import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.transition.TransitionSet.ORDERING_TOGETHER
import android.view.Gravity
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_film_details.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.common.delay
import ru.xmn.filmfilmfilm.common.dur
import ru.xmn.filmfilmfilm.common.loadUrl
import ru.xmn.filmfilmfilm.common.pairSharedTransition
import ru.xmn.filmfilmfilm.common.views.ElasticDragDismissCoordinatorLayout
import ru.xmn.filmfilmfilm.services.film.FilmData
import ru.xmn.filmfilmfilm.services.tmdb.PersonType
import java.lang.Exception


class FilmDetailsActivity : LifecycleActivity() {

    companion object {
        val POSTER_KEY = "FilmDetailsActivity.Poster"
        val FILM_ID_FOR_TMDB_KEY = "FilmDetailsActivity.Film"
    }

    lateinit private var chromeFader: ElasticDragDismissCoordinatorLayout.ElasticDragDismissCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_details)

        if (savedInstanceState == null) {
            postponeEnterTransition()
            setEnterSharedElementCallback(object : SharedElementCallback() {
                override fun onSharedElementEnd(sharedElementNames: MutableList<String>?, sharedElements: MutableList<View>?, sharedElementSnapshots: MutableList<View>?) {
                    super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
                    val visible = description_container.visibility == View.VISIBLE

                    if (visible) {
                        runExitAnimation()
                    } else {
                        runEnterAnimation()
                    }
                }
            })
        } else {
            description_container.visibility = View.VISIBLE
        }

        chromeFader = ElasticDragDismissCoordinatorLayout.SystemChromeFader(this)

        val posterUrl = intent.getStringExtra(POSTER_KEY)
        val filmImdbId = intent.getStringExtra(FILM_ID_FOR_TMDB_KEY)

        val factory = FilmDetailsViewModel.Factory(
                application, filmImdbId)

        val model = ViewModelProviders.of(this, factory).get(FilmDetailsViewModel::class.java)

        subscribeToModel(model)

        cast.layoutManager = LinearLayoutManager(this)
        crew.layoutManager = LinearLayoutManager(this)
        cast.adapter = PersonsAdapter(this, main_appbar.pairSharedTransition())
        crew.adapter = PersonsAdapter(this, main_appbar.pairSharedTransition())

        if (savedInstanceState == null)
            loadPosterThenStartTransition(posterUrl)
        else {poster.loadUrl(posterUrl)}
    }

    private fun runEnterAnimation() {
        val set = TransitionSet()
        set.addTransition(Fade().delay(200).dur(200))
        set.addTransition(Slide(Gravity.BOTTOM).dur(400))
        set.ordering = ORDERING_TOGETHER
        TransitionManager.beginDelayedTransition(description_container, set)
        description_container.visibility = View.VISIBLE
    }

    private fun runExitAnimation() {
        description_container.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        draggable.addListener(chromeFader);
    }

    override fun onPause() {
        super.onPause()
        draggable.removeListener(chromeFader);
    }

    private fun subscribeToModel(model: FilmDetailsViewModel) {
        model.filminfo.observe(this, Observer { it?.let { bindUi(it) } })
    }

    private fun bindUi(filmData: FilmData) {
        filmName.text = filmData.title
        genres.text = filmData.genres.map { it.name }.joinToString()
        info_view.text = filmData.overview
        ratings_info.text = filmData.ratings.associateBy({ it.source }, { it.value })
                .map { "${it.key}: ${it.value}" }.joinToString(separator = ", ")
        expandedImage.loadUrl(filmData.backdrop ?: "")

        (cast.adapter as PersonsAdapter).apply { items = filmData.persons.filter { it.type == PersonType.CAST.name }.map { PersonsAdapter.PersonItem(it.tmdbId, it.name ?: "", it.descr ?: "", PersonType.CAST) }.take(7) }
        (crew.adapter as PersonsAdapter).apply { items = filmData.persons.filter { it.type == PersonType.CREW.name }.map { PersonsAdapter.PersonItem(it.tmdbId, it.name ?: "", it.descr ?: "", PersonType.CREW) }.take(7) }
    }

    private fun loadPosterThenStartTransition(posterUrl: String?) {
        Glide.with(this)
                .load(posterUrl)
                .centerCrop()
                .dontAnimate()
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        startPostponedEnterTransition()
                        return false;
                    }

                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        startPostponedEnterTransition()
                        return false;
                    }
                })
                .into(poster)
    }
}