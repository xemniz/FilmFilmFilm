package ru.xmn.filmfilmfilm.screens.main.films

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import kotlinx.android.synthetic.main.fragment_one_day_films.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.common.extensions.inflate
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = container?.inflate(R.layout.fragment_one_day_films)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FilmsAdapter(activity)
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

    override fun onDestroyView() {
        super.onDestroyView()
        movieList.setAdapter(null)
    }

    fun subscribeToModel(model: FilmsFragmentViewModel) {
        model.films.observe(this, Observer({ it?.let { it1 -> showMovies(it1) } }))
    }

    fun showMovies(films: List<FilmData>) {
        (movieList.adapter as FilmsAdapter).items = films
    }

}

