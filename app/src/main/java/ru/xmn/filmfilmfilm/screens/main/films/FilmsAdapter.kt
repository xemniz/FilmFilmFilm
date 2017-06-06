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
import kotlinx.android.synthetic.main.film_item.view.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.common.inflate
import ru.xmn.filmfilmfilm.common.loadUrl
import ru.xmn.filmfilmfilm.common.ui.adapter.AutoUpdatableAdapter
import ru.xmn.filmfilmfilm.screens.filmdetails.FilmDetailsActivity
import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmItemViewData
import kotlin.properties.Delegates


class FilmsAdapter(val activity: FragmentActivity) : RecyclerView.Adapter<FilmsAdapter.ViewHolder>(), AutoUpdatableAdapter {

    var items: List<FilmItemViewData> by Delegates.observable(emptyList()) {
        _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.film_item), activity)
    }

    class ViewHolder(view: View, val activity: FragmentActivity) : RecyclerView.ViewHolder(view) {
        fun bind(film: FilmItemViewData) {
            itemView.apply {
                poster.loadUrl(film.image)
                filmName.text = film.title
                director.text = "by ${film.director}"
                ratings.text = film.Ratings.filter { it.key != "Internet Movie Database" }.map { "${it.key} : ${it.value}" }.joinToString(", ")
                genres.text = film.genres.joinToString(separator = " | ")
                setOnClickListener {
                    val intent = Intent(this@ViewHolder.itemView.context, FilmDetailsActivity::class.java)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                            Pair<View, String>(infoCard, ViewCompat.getTransitionName(infoCard)),
//                            Pair<View, String>(fragment.view?.findViewById(R.id.tabLayout), ViewCompat.getTransitionName(fragment.view?.findViewById(R.id.tabLayout))),
                            Pair<View, String>(posterCard, ViewCompat.getTransitionName(posterCard)))
                    intent.putExtra(FilmDetailsActivity.POSTER_KEY, film.image)
                    intent.putExtra(FilmDetailsActivity.FILM_IMDB_ID_KEY, film.imdbId)
                    startActivity(this@ViewHolder.itemView.context, intent, options.toBundle())
                }
            }
        }

    }
}

