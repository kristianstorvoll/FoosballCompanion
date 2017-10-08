package no.kristo.foosballcompanion.ui.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import no.kristo.foosballcompanion.R

/**
 *
 * Created by Kristian on 08.10.2017.
 */
class OnboardingLoginView : LinearLayout {

    val auth by lazy { FirebaseAuth.getInstance() }

    constructor(context: Context) : super(context) {
        internalInit()
    }

    fun internalInit() {
        inflate(context, R.layout.view_onboarding_signup, this)
    }

}
