package ru.xmn.filmfilmfilm.screens.main.onedayfilms

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import kotlinx.android.synthetic.main.fragment_one_day_films.*

class OneDayFilmsFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        movieList.adapter = OneDayFilmsAdapter()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
