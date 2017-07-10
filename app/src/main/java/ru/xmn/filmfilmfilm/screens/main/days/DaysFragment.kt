package ru.xmn.filmfilmfilm.screens.main.days

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import khronos.Dates
import khronos.days
import khronos.plus
import khronos.toString
import kotlinx.android.synthetic.main.fragment_days.*
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.common.extensions.inflate
import ru.xmn.filmfilmfilm.screens.main.films.FilmsFragment

class DaysFragment : LifecycleFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = container?.inflate(R.layout.fragment_days)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        daysPager?.apply {
            adapter = DaysAdapter(activity.supportFragmentManager)
        }
        tabLayout.setupWithViewPager(daysPager)

        activity.setActionBar(toolbar)
        activity.actionBar.title = "В КИНОТЕАТРАХ МОСКВЫ"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}

class DaysAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = FilmsFragment.withDaysOffset(position)

    override fun getCount() = 10

    override fun getPageTitle(position: Int): CharSequence =
            when (position) {
                0 -> "Сегодня"
                1 -> "Завтра"
                else -> position.offsetToDateTitle()
            }

    private fun Int.offsetToDateTitle(): CharSequence {
        val date = Dates.now + this.days
        return date.toString(format = "EEE MMM dd")
    }
}


