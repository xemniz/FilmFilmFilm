package ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhuinden.servicetree.ServiceTree
import kotlinx.android.synthetic.main.fragment_one_day_films.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.application.App
import ru.xmn.filmfilmfilm.common.inflate
import ru.xmn.filmfilmfilm.common.ui.HasServices
import ru.xmn.filmfilmfilm.screens.main.MainActivity
import ru.xmn.filmfilmfilm.screens.main.MainActivityComponent
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.OneDayFilmsAdapter
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.di.OneDayFilmsModule
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.viewmodels.FilmItemViewModel
import javax.inject.Inject

class OneDayFilmsFragment : Fragment(), HasServices, OneDayFilmView {

    @Inject
    lateinit var presenter: OneDayFilmPresenter

    override fun showMovies(films: List<FilmItemViewModel>) {
        (movieList?.adapter as? OneDayFilmsAdapter)?.items = films
    }

    override fun showError(error: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(stringResId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(srtResId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNodeTag(): String = "OneDayFilmsFragment"

    override fun bindServices(node: ServiceTree.Node) {
        val mainComponent: MainActivityComponent = node.getService(App.DAGGER_COMPONENT)
        val component = mainComponent.plus(OneDayFilmsModule())
        node.bindService(App.DAGGER_COMPONENT, component)
        component.inject(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val mainActivity = activity as MainActivity
        mainActivity.registerFragmentServices(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = container?.inflate(R.layout.fragment_one_day_films)
        movieList?.apply {
            adapter = OneDayFilmsAdapter()
            layoutManager = LinearLayoutManager(context)
        }
        presenter.loadMovies()

        return view
    }
}

