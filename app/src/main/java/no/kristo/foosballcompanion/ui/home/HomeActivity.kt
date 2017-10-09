package no.kristo.foosballcompanion.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.view.menu.MenuItemImpl
import android.view.Menu
import kotlinx.android.synthetic.main.activity_home.*
import no.kristo.foosballcompanion.R
import no.kristo.foosballcompanion.extensions.toast
import no.kristo.foosballcompanion.model.AppModel
import no.kristo.foosballcompanion.model.UserStore
import no.kristo.foosballcompanion.ui.onboarding.OnboardingActivity

/**
 * Created by Kristian on 08.10.2017.
 */
class HomeActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        if (AppModel.instance.currentUser == null) {
            return startActivity(OnboardingActivity.newIntent(this)).also { finish() }
        } else {
            toast("Heisann ${AppModel.instance.currentUser?.displayName ?: ""}")
        }

        setupUi()
    }

    fun setupUi() {
        setupBottomNavigation()
    }

    fun setupBottomNavigation() {

    }
}