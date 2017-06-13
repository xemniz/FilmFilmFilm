package ru.xmn.filmfilmfilm.screens.persondetails

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_person_detail.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.screens.main.films.FilmsAdapter
import ru.xmn.filmfilmfilm.services.tmdb.PersonType

class PersonDetailsActivity : LifecycleActivity() {

    companion object {
        val PERSON_ID = "PersonDetailsActivity.PersonId"
        val PERSON_TYPE = "PersonDetailsActivity.PersonType"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)
        person_films.layoutManager = LinearLayoutManager(this)

        val personId = intent.getStringExtra(PERSON_ID)
        val personType = intent.getSerializableExtra(PERSON_TYPE) as PersonType

        val factory = PersonDetailsViewModel.Factory(
                application, personId, personType)

        val model = ViewModelProviders.of(this, factory).get(PersonDetailsViewModel::class.java)

        subscribeToModel(model)
    }

    private fun subscribeToModel(model: PersonDetailsViewModel) {
        model.films.observe(this, Observer{person_films.adapter = it?.let { it1 -> FilmsAdapter(this, it1) } })
    }
}