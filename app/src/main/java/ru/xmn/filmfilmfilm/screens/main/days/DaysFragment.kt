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
import ru.xmn.filmfilmfilm.common.inflate
import ru.xmn.filmfilmfilm.common.ui.ToolbarOwner
import ru.xmn.filmfilmfilm.screens.main.films.FilmsFragment

class DaysFragment : LifecycleFragment(), ToolbarOwner {
    override val toolbar: View?
        get() = daysPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = container?.inflate(R.layout.fragment_days)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        daysPager?.apply {
            adapter = DaysAdapter(activity.supportFragmentManager, this@DaysFragment)
        }
        tabLayout.setupWithViewPager(daysPager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}

class DaysAdapter(fm: FragmentManager, val toolbarOwner: ToolbarOwner) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = FilmsFragment.withDaysOffset(position, toolbarOwner)

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


