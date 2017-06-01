package ru.xmn.filmfilmfilm.common.views

import android.content.Context
import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import ru.xmn.filmfilmfilm.common.abs

class FilmTabLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : TabLayout(context, attrs, defStyleAttr) {

    constructor (context: Context) : this(context, null)

    constructor (context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val textViews = mutableListOf<FilmTabTextView>()

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
                val pagePosition = page?.getTag() as? Int
                if (pagePosition == null) {
                    Log.v("FilmTabLayout", "working only with views in viewpager with Int tags")
                    return
                }

                when {
                    position <= -1.0f || position >= 1.0f -> textViews[pagePosition].scaleText(0F)
                    else -> textViews[pagePosition].scaleText(1 - position.abs())
                }
            }

        })
    }
}


private class FilmTabTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : TextView(context, attrs, defStyleAttr) {

    var scaleCoeff = 2F
    private var commonTextSize: Float = 0F
    private var selectedTextSize: Float = 0F

    fun changeTextSize(size: Float) {
        super.setTextSize(size)
        commonTextSize = size
        selectedTextSize = size * scaleCoeff
    }

    constructor (context: Context) : this(context, null)

    constructor (context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        changeTextSize(15F)
        setTextColor(Color.WHITE)
    }

    fun scaleText(positionOffset: Float) {
        textSize = commonTextSize + (selectedTextSize - commonTextSize) * positionOffset
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}