package ru.xmn.filmfilmfilm.screens.filmdetails

import android.arch.lifecycle.LifecycleActivity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_film_details.*
import ru.xmn.filmfilmfilm.R
import java.lang.Exception


class FilmDetailsActivity : LifecycleActivity() {

    companion object {
        val POSTER_KEY = "FilmDetailsActivity.Poster"
        val FILM_IMDB_ID_KEY = "FilmDetailsActivity.Film"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_details)
        postponeEnterTransition()

        val posterUrl = intent.getStringExtra(POSTER_KEY)
        val filmImdbId = intent.getStringExtra(FILM_IMDB_ID_KEY)

        loadPosterThenStartTransition(posterUrl)
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
