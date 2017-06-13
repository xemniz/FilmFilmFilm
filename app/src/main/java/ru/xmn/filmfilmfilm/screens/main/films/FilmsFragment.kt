package ru.xmn.filmfilmfilm.screens.main.films

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_one_day_films.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.common.inflate
import ru.xmn.filmfilmfilm.services.film.FilmData
import ru.xmn.filmfilmfilm.screens.main.films.viewmodels.FilmsFragmentViewModel

class FilmsFragment : LifecycleFragment() {

    companion object {
        val DAYS_OFFSET = "days_offset"

        fun withDaysOffset(offset: Int): FilmsFragment {
            val fragment = FilmsFragment()
            val args = Bundle()
            args.putInt(DAYS_OFFSET, offset)
            fragment.setArguments(args)
            return fragment
        }
    }

    fun showMovies(films: List<FilmData>) {
        (movieList.adapter as FilmsAdapter).items = films
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = container?.inflate(R.layout.fragment_one_day_films)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FilmsAdapter(this@FilmsFragment.activity)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val offset = arguments.getInt(DAYS_OFFSET)
        view?.setTag(offset)
        val factory = FilmsFragmentViewModel.Factory(
                activity.application, offset)

        val model = ViewModelProviders.of(this, factory)
                .get(offset.toString(), FilmsFragmentViewModel::class.java)

        subscribeToModel(model)
    }

    fun subscribeToModel(model: FilmsFragmentViewModel) {
        model.films.observe(this, Observer({ (movieList?.adapter as FilmsAdapter).items = it ?: listOf() }))
    }

}

