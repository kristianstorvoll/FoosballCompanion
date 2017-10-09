package no.kristo.foosballcompanion.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.view_onboarding_item.view.*
import kotterknife.bindView
import no.kristo.foosballcompanion.R


/**
 * Data class descrigin the onboarding item
 */
data class OnboardingItem(val title: String = "", val description: String = "", val image: Drawable?)

/**
 * View rendering a OnboardingItem
 * Created by Kristian on 07.10.2017.
 */
class OnboardingItemView : LinearLayout {

    constructor(context: Context) : super(context) {
        internalInit()
    }

    private fun internalInit() {
        inflate(context, R.layout.view_onboarding_item, this)
    }

    fun renderOnboardingItem(item: OnboardingItem) {
        onboarding_item_title.text = item.title
        onboarding_item_description.text = item.description
        //onboarding_item_image.setImageDrawable(item.image)
    }
}
