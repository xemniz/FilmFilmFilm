package ru.xmn.filmfilmfilm.screens.main.days

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.ViewModelProviders
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
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.FilmsFragment
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.FilmsFragmentViewModel

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        val model = ViewModelProviders.of(this).get(FilmsFragmentViewModel::class.java)

//        subscribeToModel(model)
    }

    fun subscribeToModel(model: FilmsFragmentViewModel) {
//        model.films.observe(this, Observer({ (movieList.adapter as OneDayFilmsAdapter).items = it ?: listOf() }))
    }

}

class DaysAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = FilmsFragment.withDaysOffset(position)

    override fun getCount() = 10

    override fun getPageTitle(position: Int): CharSequence =
        when(position){
            0 -> "Today"
            1 -> "Tomorrow"
            else -> position.offsetToDateTitle()
        }

    private fun Int.offsetToDateTitle(): CharSequence {
        val date = Dates.now + this.days
        return date.toString(format = "EEE MMM dd")
    }
}


