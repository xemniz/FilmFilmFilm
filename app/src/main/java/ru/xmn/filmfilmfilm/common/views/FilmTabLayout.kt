package ru.xmn.filmfilmfilm.common.views

import android.content.Context
import android.support.design.widget.TabLayout
import android.util.AttributeSet

class FilmTabLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : TabLayout(context, attrs, defStyleAttr) {

    constructor (context: Context) : this(context, null)

    constructor (context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun setScrollPosition(position: Int, positionOffset: Float, updateSelectedText: Boolean) {
        super.setScrollPosition(position, positionOffset, updateSelectedText)
    }
}