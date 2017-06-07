package ru.xmn.filmfilmfilm.screens

import android.app.Application
import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_person_detail.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.application.App
import ru.xmn.filmfilmfilm.screens.main.MainActivityModule
import ru.xmn.filmfilmfilm.screens.main.films.FilmsAdapter
import ru.xmn.filmfilmfilm.screens.main.films.FilmsProvider
import ru.xmn.filmfilmfilm.screens.main.films.di.FilmsModule
import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmItemViewData
import javax.inject.Inject

class PersonDetailsActivity : LifecycleActivity() {

    companion object {
        val PERSON_ID = "PersonDetailsActivity.PersonId"
        val PERSON_TYPE = "PersonDetailsActivity.PersonType"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)
        person_films.layoutManager = LinearLayoutManager(this)
        person_films.adapter = FilmsAdapter(this)

        val personId = intent.getStringExtra(PERSON_ID)
        val personType = intent.getStringExtra(PERSON_TYPE)

        val factory = PersonDetailsViewModel.Factory(
                application, personId, personType)

        val model = ViewModelProviders.of(this, factory).get(PersonDetailsViewModel::class.java)

        subscribeToModel(model)
    }

    private fun subscribeToModel(model: PersonDetailsViewModel?) {
        model?.loadMovies()
    }
}

class PersonDetailsViewModel(val application: Application, val personId: String, val personType: String) : ViewModel() {
    val films = MutableLiveData<List<FilmItemViewData>>()

    @Inject
    lateinit var filmsProvider: FilmsProvider

    init {
        App.component.plus(MainActivityModule()).plus(FilmsModule()).inject(this)
        loadMovies()
    }

    fun loadMovies() {
        val type = PersonType.valueOf(personType)
        val moviesForPerson = when(type){
            PersonType.CREW -> filmsProvider.getMoviesForCrew(personId)
            PersonType.CAST -> filmsProvider.getMoviesForCast(personId)
            else -> filmsProvider.getMoviesForPerson(personId)
        }
        moviesForPerson.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ films.value = it }, { it.printStackTrace() })
    }

    class Factory(val application: Application, val personId: String, val personType: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
            return PersonDetailsViewModel(application, personId, personType) as T
        }
    }
}

enum class PersonType {
    CREW, CAST
}
