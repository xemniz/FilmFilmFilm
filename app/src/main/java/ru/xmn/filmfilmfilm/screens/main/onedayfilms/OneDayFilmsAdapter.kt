package ru.xmn.filmfilmfilm.screens.main.onedayfilms

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.common.ui.adapter.AutoUpdatableAdapter
import ru.xmn.filmfilmfilm.common.inflate
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.viewmodels.FilmItemViewModel
import kotlin.properties.Delegates
import kotlinx.android.synthetic.main.film_item.view.*
import ru.xmn.filmfilmfilm.common.loadUrl

class OneDayFilmsAdapter : RecyclerView.Adapter<OneDayFilmsAdapter.ViewHolder>(), AutoUpdatableAdapter {

    var items: List<FilmItemViewModel> by Delegates.observable(emptyList()) {
        _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.film_item))
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(film: FilmItemViewModel) {
            itemView.poster.loadUrl(film.image)
        }

    }
}

