package ru.xmn.filmfilmfilm.common.views

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import ru.xmn.filmfilmfilm.common.abs

class FilmTabLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : TabLayout(context, attrs, defStyleAttr) {

    constructor (context: Context) : this(context, null)

    constructor (context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    val textViews = mutableListOf<FilmTabTextView>()

    override fun setupWithViewPager(viewPager: ViewPager?) {
        super.setupWithViewPager(viewPager)
        for (i in 0..tabCount - 1) {
            val tab = getTabAt(i)
            val view = FilmTabTextView(this.context)
            textViews += view
            view.id = android.R.id.text1
            tab?.customView = view
        }
        viewPager?.setPageTransformer(false, object : ViewPager.PageTransformer {
            override fun transformPage(page: View?, position: Float) {
                // Get page index from tag
                val pagePosition = page?.getTag() as Int

                when {
                    position <= -1.0f || position >= 1.0f -> textViews[pagePosition].transformTo(0F)
                    else -> textViews[pagePosition].transformTo(1 - position.abs())
                }

            }

        })
    }
}

class FilmTabTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : TextView(context, attrs, defStyleAttr) {

    constructor (context: Context) : this(context, null)

    constructor (context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    fun transformTo(positionOffset: Float) {
        textSize = 15 + 5 * positionOffset
    }
}