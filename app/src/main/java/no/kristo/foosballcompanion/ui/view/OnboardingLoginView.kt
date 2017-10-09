package no.kristo.foosballcompanion.ui.view

import android.content.Context
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.view_onboarding_signup.view.*
import no.kristo.foosballcompanion.R

/**
 *
 * Created by Kristian on 08.10.2017.
 */
class OnboardingLoginView : LinearLayout {

    constructor(context: Context) : super(context) {
        internalInit()
    }

    fun internalInit() {
        inflate(context, R.layout.view_onboarding_signup, this)
    }

    fun onFacebookButtonClicked(cb: () -> Unit) {
        this.facebookButton.setOnClickListener { cb() }
    }

}
