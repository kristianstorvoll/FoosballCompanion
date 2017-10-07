package no.kristo.foosballcompanion.ui

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_onboarding.*
import no.kristo.foosballcompanion.R

/**
 * Created by Kristian on 07.10.2017.
 */


class OnboardingActivity : AppCompatActivity() {

    var adapter: PagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        setupViewPager()
    }

    /**
     * Setup the onboarding viewpager
     */
    private fun setupViewPager() {
        adapter = OnboardingAdapter();
        viewPager.adapter = adapter
    }
}

class OnboardingAdapter(val views: ArrayList<View> = ArrayList<View>()) : PagerAdapter() {

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return true
    }

    override fun getCount(): Int {
        return views.count()
    }


}