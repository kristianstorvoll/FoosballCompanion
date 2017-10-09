package no.kristo.foosballcompanion.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_onboarding.*
import no.kristo.foosballcompanion.R
import no.kristo.foosballcompanion.ui.view.OnboardingItem
import no.kristo.foosballcompanion.ui.view.OnboardingItemView
import no.kristo.foosballcompanion.ui.view.OnboardingLoginView
import no.kristo.foosballcompanion.ui.home.HomeActivity
import no.kristo.foosballcompanion.ui.view.ViewPagerAdapter


/**
 * Displays some short information and a sign-up page at the end
 * Created by Kristian on 07.10.2017.
 */
class OnboardingActivity : AppCompatActivity(), LoginDelegate.LoginDelegateListener {

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, OnboardingActivity::class.java)
            return intent
        }
    }

    val adapter by lazy { ViewPagerAdapter() }
    val loginView by lazy { OnboardingLoginView(this) }

    var loginDelegate: LoginDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        setupToolbar()
        setupViewPager()

        nextButton.setOnClickListener { goToNextPage() }
        prevButton.setOnClickListener { goToPreviousPage() }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginDelegate?.onActivityResult(requestCode, resultCode, data)
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
        loginView.onFacebookButtonClicked {
            loginDelegate = FacebookLoginDelegate(this, this)
            loginDelegate?.signIn()
        }

        adapter.notifyDataSetChanged()
    }

    private fun generateOnboardingViews(): Collection<View> {
        //create 3 views and render an onboarding item for each of them
        return (1..3).map { i -> OnboardingItemView(this).also { it.renderOnboardingItem(OnboardingItem("Hello #$i", "Description baby", null)) } }
    }

    override val complete: (Boolean) -> Unit = {
        startActivity(HomeActivity.newIntent(this))
        finish()
    }
    override val error: (Throwable) -> Unit = {

    }
}