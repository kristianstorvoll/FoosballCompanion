package no.kristo.foosballcompanion.ui

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_onboarding.*
import no.kristo.foosballcompanion.R
import no.kristo.foosballcompanion.ui.view.OnboardingItem
import no.kristo.foosballcompanion.ui.view.OnboardingItemView
import no.kristo.foosballcompanion.ui.view.OnboardingLoginView

/**
 * Displays some short information and a sign-up page at the end
 * Created by Kristian on 07.10.2017.
 */
class OnboardingActivity : AppCompatActivity() {

    val adapter by lazy { MyAdapter() }
    val loginView by lazy { OnboardingLoginView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        setupToolbar()
        setupViewPager()

        nextButton.setOnClickListener { goToNextPage() }
        prevButton.setOnClickListener { goToPreviousPage() }
    }

    fun setupToolbar() {
        val add = toolbar.menu.add("Skip")
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        add.setOnMenuItemClickListener {
            goToLastPage()
            return@setOnMenuItemClickListener true
        }
    }

    fun validateToolbarButtons() {
        toolbar.menu.getItem(0).isVisible = viewPager.currentItem != adapter.count-1
    }

    fun goToNextPage() {
        val next = viewPager.currentItem + 1
        if (adapter.count-1 >= next) {
            viewPager.setCurrentItem(next, true)
        }
    }

    fun goToPreviousPage() {
        val prev = viewPager.currentItem - 1
        if (0 <= prev) {
            viewPager.setCurrentItem(prev, true)
        }
    }

    private fun goToLastPage() {
        viewPager.setCurrentItem(adapter.count - 1, true)
    }

    private fun setupViewPager() {
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int): Unit = validateToolbarButtons()
        })

        tabLayout.setupWithViewPager(viewPager)
        adapter.views.addAll(generateOnboardingViews())
        adapter.views.add(loginView)
        adapter.notifyDataSetChanged()
    }

    private fun generateOnboardingViews(): Collection<View> {
        val views = ArrayList<View>()
        for (i in 1..3) {
            val v = OnboardingItemView(this).also { it.renderOnboardingItem(OnboardingItem("Hello #$i", "Description baby", null)) }
            views.add(v)
        }

        return views
    }
}

class MyAdapter(val views: MutableList<View> = ArrayList()) : PagerAdapter() {

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