package no.kristo.foosballcompanion.ui

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_onboarding.*
import no.kristo.foosballcompanion.R
import no.kristo.foosballcompanion.ui.view.OnboardingItem
import no.kristo.foosballcompanion.ui.view.OnboardingItemView

/**
 * Displays some short information and a sign-up page at the end
 * Created by Kristian on 07.10.2017.
 */
class OnboardingActivity : AppCompatActivity() {

    var adapter: OnboardingAdapter = OnboardingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        setupViewPager()
    }

    /**
     * Setup the onboarding viewpager
     */
    private fun setupViewPager() {
        //generate the views
        adapter.views.addAll(generateOnboardingViews())
        adapter.notifyDataSetChanged()

        //set the adapter
        viewPager.adapter = adapter
    }

    private fun generateOnboardingViews(): Collection<View> {
        val views = ArrayList<View>()

        OnboardingItemView(this).also { it.renderOnboardingItem(OnboardingItem("Test", "Test desc" ,null))}.also { adapter.views.add(it) }

        return views
    }
}

class OnboardingAdapter(val views: ArrayList<View> = ArrayList<View>()) : PagerAdapter() {

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return views.count()
    }

    override fun instantiateItem(container: ViewGroup?, position: Int) {
        val view = views.get(position)
        container?.addView(view)
    }


}