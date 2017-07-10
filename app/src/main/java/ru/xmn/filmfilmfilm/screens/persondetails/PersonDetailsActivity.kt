package ru.xmn.filmfilmfilm.screens.persondetails

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import io.realm.OrderedRealmCollection
import kotlinx.android.synthetic.main.activity_person_detail.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.common.views.ElasticDragDismissFrameLayout
import ru.xmn.filmfilmfilm.screens.main.films.FilmsAdapter
import ru.xmn.filmfilmfilm.services.film.FilmData
import ru.xmn.filmfilmfilm.services.tmdb.PersonType

class PersonDetailsActivity : LifecycleActivity() {

    companion object {
        val PERSON_ID = "PersonDetailsActivity.PersonId"
        val PERSON_TYPE = "PersonDetailsActivity.PersonType"
        val PERSON_NAME = "PersonDetailsActivity.PersonName"
    }

    lateinit private var chromeFader: ElasticDragDismissFrameLayout.SystemChromeFader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)
        person_films.layoutManager = LinearLayoutManager(this)
        person_films.adapter = FilmsAdapter(this)
        setActionBar(toolbar)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        chromeFader = ElasticDragDismissFrameLayout.SystemChromeFader(this)

        val personId = intent.getStringExtra(PERSON_ID)
        val personType = intent.getSerializableExtra(PERSON_TYPE) as PersonType
        val personName = intent.getStringExtra(PERSON_NAME)

        actionBar.title = personName

        val factory = PersonDetailsViewModel.Factory(
                application, personId, personType)

        val model = ViewModelProviders.of(this, factory).get(PersonDetailsViewModel::class.java)

        subscribeToModel(model)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        root.addListener(chromeFader);
    }

    override fun onPause() {
        super.onPause()
        root.removeListener(chromeFader);
    }

    override fun onDestroy() {
        super.onDestroy()
        person_films.setAdapter(null);
    }

    private fun subscribeToModel(model: PersonDetailsViewModel) {
        model.films.observe(this, Observer {  it?.let { bindUi(it)  } })
    }

    private fun bindUi(it: List<FilmData>){
        (person_films.adapter as FilmsAdapter).items = it
    }
}