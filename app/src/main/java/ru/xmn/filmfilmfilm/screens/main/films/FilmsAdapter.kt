package ru.xmn.filmfilmfilm.screens.main.films

import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.film_item.view.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.common.inflate
import ru.xmn.filmfilmfilm.common.loadUrl
import ru.xmn.filmfilmfilm.common.pairSharedTransition
import ru.xmn.filmfilmfilm.screens.filmdetails.FilmDetailsActivity
import ru.xmn.filmfilmfilm.services.film.FilmData


class FilmsAdapter(val activity: FragmentActivity, data: OrderedRealmCollection<FilmData>, val toolbar: View) : RealmRecyclerViewAdapter<FilmData, FilmsAdapter.ViewHolder>(data, true) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.film_item), activity, toolbar)

    class ViewHolder(view: View, val activity: FragmentActivity, val toolbar: View) : RecyclerView.ViewHolder(view) {
        fun bind(film: FilmData?) {
            if (film == null) return
            itemView.apply {
                film.image?.let { poster.loadUrl(it) }
                filmName.text = film.title
                director.text = "by ${film.director}"
                ratings.text = film.ratings.filter { it.source != "Internet Movie Database" }.map { "${it.source} : ${it.value}" }.joinToString(", ")
                genres.text = film.genres.map { it.name }.joinToString(separator = " | ")
                setOnClickListener {
                    val intent = Intent(this@ViewHolder.itemView.context, FilmDetailsActivity::class.java)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                            infoCard.pairSharedTransition(),
                            posterCard.pairSharedTransition(),
                            toolbar.pairSharedTransition())
                    intent.putExtra(FilmDetailsActivity.POSTER_KEY, film.image)
                    intent.putExtra(FilmDetailsActivity.FILM_ID_FOR_TMDB_KEY, film.imdbId ?: film.tmdbId)
                    startActivity(this@ViewHolder.itemView.context, intent, options.toBundle())
                }
            }
        }

    }
}

