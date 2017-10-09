package no.kristo.foosballcompanion.ui.view

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * Created by Kristian on 08.10.2017.
 */
class ViewPagerAdapter(val views: MutableList<View> = ArrayList()) : PagerAdapter() {

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return views.size
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        container?.addView(views[position])
        return views[position]
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View?)
    }
}