package ru.xmn.filmfilmfilm.screens.filmdetails

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.common.inflate
import ru.xmn.filmfilmfilm.common.ui.adapter.AutoUpdatableAdapter
import kotlin.properties.Delegates
import kotlinx.android.synthetic.main.person_item.view.*

class PeoplesAdapter : RecyclerView.Adapter<PeoplesAdapter.ViewHolder>(), AutoUpdatableAdapter {

    var items: List<PersonItem> by Delegates.observable(emptyList()) {
        _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PeoplesAdapter.ViewHolder, position: Int) = holder.bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeoplesAdapter.ViewHolder = PeoplesAdapter.ViewHolder(parent.inflate(R.layout.person_item))

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(personData: PersonItem) {
            itemView.apply {
                person_name.text = personData.name
                person_description.text = personData.descr
            }
        }

    }

    class PersonItem(val name: String, val descr: String)
}

