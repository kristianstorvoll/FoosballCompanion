package no.kristo.foosballcompanion.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import no.kristo.foosballcompanion.R
import no.kristo.foosballcompanion.extensions.onResultObservers
import no.kristo.foosballcompanion.extensions.toast
import no.kristo.foosballcompanion.model.AppModel
import no.kristo.foosballcompanion.model.NfcDevice
import no.kristo.foosballcompanion.nfc.NfcDelegateImpl
import no.kristo.foosballcompanion.nfc.NfcDelgate
import no.kristo.foosballcompanion.ui.devices.AddNfcDeviceDelegate
import no.kristo.foosballcompanion.ui.devices.MyDevicesView
import no.kristo.foosballcompanion.ui.onboarding.OnboardingActivity
import no.kristo.foosballcompanion.ui.profile.ProfileView
import no.kristo.foosballcompanion.ui.view.ViewPagerAdapter


class HomeActivity : AppCompatActivity(), AddNfcDeviceDelegate {


    val adapter: ViewPagerAdapter by lazy { ViewPagerAdapter() }

    lateinit var nfcDelegate: NfcDelgate

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            return intent
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (AppModel.instance.currentUser == null) return startActivity(OnboardingActivity.newIntent(this)).also { finish() }

        nfcDelegate = NfcDelegateImpl(this)
        onResultObservers.add(nfcDelegate)

        setupUi()
    }

    fun setupUi() {
        setupViewPager()
        setupBottomNavigation()
    }

    fun setupViewPager() {
        viewPager.adapter = adapter

        MyDevicesView(this).also {
            lifecycle.addObserver(it)

            it.addDeviceDelegate = this
            it.checkNfcAvailabilityBlock = {
                if (nfcDelegate.isNfcAvailable()) toast("NFC AVAILABLE :)") else toast("NFC NOT AVAILABLE :(")
            }
            adapter.views.add(it)
        }

        ProfileView(this).also {
            lifecycle.addObserver(it)
            AppModel.instance.currentUser?.let { user -> it.renderUser(user) }
            it.onSignoutClicked { doSignOut() }
            adapter.views.add(it)
        }

        adapter.notifyDataSetChanged()
    }

    fun setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_profile -> {
                    viewPager.setCurrentItem(1, true)
                    true //return true
                }
                R.id.action_home -> {
                    viewPager.setCurrentItem(0, true)
                    true
                }
                else -> false //not handled, return false
            }

            true
        }
    }

    fun doSignOut() {
        AppModel.instance.signOut().also { startActivity(OnboardingActivity.newIntent(this)) }.also { finish() }
    }


    override fun addDevice(completion: (NfcDevice) -> Unit) {

    }
}

interface Cleanup {
    fun cleanup()
}

