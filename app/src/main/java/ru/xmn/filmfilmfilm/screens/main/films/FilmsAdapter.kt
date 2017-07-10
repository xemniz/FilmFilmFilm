package ru.xmn.filmfilmfilm.screens.main.films

import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.film_item.view.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.common.extensions.inflate
import ru.xmn.filmfilmfilm.common.extensions.loadUrl
import ru.xmn.filmfilmfilm.common.extensions.pairSharedTransition
import ru.xmn.filmfilmfilm.common.ui.adapter.AutoUpdatableAdapter
import ru.xmn.filmfilmfilm.screens.filmdetails.FilmDetailsActivity
import ru.xmn.filmfilmfilm.services.film.FilmData
import kotlin.properties.Delegates


class FilmsAdapter(val activity: FragmentActivity) : RecyclerView.Adapter<FilmsAdapter.ViewHolder>(), AutoUpdatableAdapter {

    var items: List<FilmData> by Delegates.observable(emptyList()) {
        _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.film_item), activity)

    class ViewHolder(view: View, val activity: FragmentActivity) : RecyclerView.ViewHolder(view) {
        fun bind(film: FilmData?) {
            if (film == null) return
            itemView.apply {
                film.image?.let { poster.loadUrl(it) }
                filmName.text = film.title
                when {
                    film.director != null -> director.text = "by ${film.director}"
                    else -> film.persons.filter { it.descr == "Director" }.getOrNull(0)?.let { director.text = "by ${it}" }
                }
                ratings.text = film.ratings.filter { it.source != "Internet Movie Database" }.map { "${it.source} : ${it.value}" }.joinToString(", ")
                genres.text = film.genres.map { it.name }.joinToString(separator = " | ")
                setOnClickListener {
                    val intent = Intent(this@ViewHolder.itemView.context, FilmDetailsActivity::class.java)
                    val args = arrayOf(infoCard.pairSharedTransition(), posterCard.pairSharedTransition())
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *args)
                    intent.putExtra(FilmDetailsActivity.POSTER_KEY, film.image)
                    intent.putExtra(FilmDetailsActivity.FILM_ID_FOR_TMDB_KEY, film.imdbId ?: film.tmdbId)
                    startActivity(this@ViewHolder.itemView.context, intent, options.toBundle())
                }
            }
        }

    }
}

