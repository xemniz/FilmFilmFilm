package ru.xmn.filmfilmfilm.screens.main.onedayfilms

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
import ru.xmn.filmfilmfilm.common.ui.HasServices
import ru.xmn.filmfilmfilm.screens.main.MainActivityComponent

class OneDayFilmsFragment : Fragment(), HasServices{
    override fun getNodeTag(): String = "OneDayFilmsFragment"

    override fun bindServices(node: ServiceTree.Node) {
        val mainComponent: MainActivityComponent = node.getService(App.DAGGER_COMPONENT)
        node.bindService(App.DAGGER_COMPONENT, mainComponent.plus(OneDayFilmsModule()))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_one_day_films, container, false)
        movieList.apply {
            adapter = OneDayFilmsAdapter()
            layoutManager = LinearLayoutManager(context)
        }
        return view
    }
}

