package no.kristo.foosballcompanion.ui.view

import android.content.Context
import android.widget.LinearLayout
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
}
