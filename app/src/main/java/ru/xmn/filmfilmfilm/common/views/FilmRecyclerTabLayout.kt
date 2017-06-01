package ru.xmn.filmfilmfilm.common.views

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nshmura.recyclertablayout.RecyclerTabLayout
import ru.xmn.filmfilmfilm.common.abs

class FilmRecyclerTabLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerTabLayout(context, attrs, defStyleAttr) {
    val provider = Provider()

    constructor (context: Context) : this(context, null)

    constructor (context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val textViews = mutableListOf<FilmRecyclerTabTextView>()

    fun setUp(daysPager: ViewPager) {
        setUpWithAdapter(CustomRecyclerViewAdapter(daysPager, provider))


        daysPager.setPageTransformer(false, object : ViewPager.PageTransformer {
            override fun transformPage(page: View?, position: Float) {
                // Get page index from tag
                val pagePosition = page?.getTag() as? Int
                if (pagePosition == null) {
                    return
                }

                when {
                    position <= -1.0f || position >= 1.0f -> provider.push(pagePosition, 0F)
                    else -> provider.push(pagePosition, 1 - position.abs())
                }
            }

        })
    }

}


private class FilmRecyclerTabTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : TextView(context, attrs, defStyleAttr) {

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
    }

    fun scaleText(positionOffset: Float) {
        textSize = commonTextSize + (selectedTextSize - commonTextSize) * positionOffset
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}

class CustomRecyclerViewAdapter(viewPager: ViewPager, val provider: Provider) : RecyclerTabLayout.Adapter<CustomRecyclerViewAdapter.ViewHolder>(viewPager) {
    override fun getItemCount(): Int {
        return viewPager.adapter.count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        return ViewHolder(
                FilmRecyclerTabTextView(parent.context), provider
        )
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        (holder as ViewHolder).bind(viewPager, position)
    }

    class ViewHolder(view: View, val provider: Provider) : RecyclerView.ViewHolder(view) {
        var listener: ((Int, Float) -> Unit)? = null

        fun bind(viewPager: ViewPager, position: Int) {
            (itemView as FilmRecyclerTabTextView).text = viewPager.adapter.getPageTitle(position)

            listener?.let { provider.remove(it) }

            listener = {
                page, pos ->
                if (page == position)
                    itemView.scaleText(pos)
            }

            provider.register(listener!!)
        }
    }
}

class Provider {
    val listeners = mutableListOf<(page: Int, position: Float) -> Unit>()

    fun remove(listener: (page: Int, position: Float) -> Unit) {
        listeners.remove(listener)
    }

    fun register(listener: (page: Int, position: Float) -> Unit) {
        listeners.add(listener)
    }

    fun push(page: Int, position: Float) {
        listeners.forEach { it(page, position) }
    }
}

