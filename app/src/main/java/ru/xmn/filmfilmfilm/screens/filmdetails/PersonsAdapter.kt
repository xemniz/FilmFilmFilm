package ru.xmn.filmfilmfilm.screens.filmdetails

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.film_item.view.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.common.inflate
import ru.xmn.filmfilmfilm.common.ui.adapter.AutoUpdatableAdapter
import kotlin.properties.Delegates
import kotlinx.android.synthetic.main.person_item.view.*
import ru.xmn.filmfilmfilm.common.pairSharedTransition
import ru.xmn.filmfilmfilm.screens.persondetails.PersonDetailsActivity
import ru.xmn.filmfilmfilm.services.tmdb.PersonType

class PersonsAdapter(val activity: Activity, vararg val args: Pair<View, String>) : RecyclerView.Adapter<PersonsAdapter.ViewHolder>(), AutoUpdatableAdapter {

    var items: List<PersonItem> by Delegates.observable(emptyList()) {
        _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PersonsAdapter.ViewHolder, position: Int) = holder.bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonsAdapter.ViewHolder = PersonsAdapter.ViewHolder(parent.inflate(R.layout.person_item), activity, *args )

    class ViewHolder(view: View, val activity: Activity, vararg val args: Pair<View, String>) : RecyclerView.ViewHolder(view) {
        fun bind(personData: PersonItem) {
            itemView.apply {
                person_name.text = personData.name
                person_description.text = personData.descr
                setOnClickListener {
                    val intent = Intent(this@ViewHolder.itemView.context, PersonDetailsActivity::class.java)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, person_item_wrapper.pairSharedTransition(), *args)
                    intent.putExtra(PersonDetailsActivity.PERSON_ID, personData.id)
                    intent.putExtra(PersonDetailsActivity.PERSON_TYPE, personData.type)
                    ContextCompat.startActivity(this@ViewHolder.itemView.context, intent, options.toBundle())
                }
            }
        }
    }

    class PersonItem(val id: String, val name: String, val descr: String, val type: PersonType)
}

