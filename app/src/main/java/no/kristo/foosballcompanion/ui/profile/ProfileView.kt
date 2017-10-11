package no.kristo.foosballcompanion.ui.profile

import android.arch.lifecycle.DefaultLifecycleObserver
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_profile.view.*
import no.kristo.foosballcompanion.R
import no.kristo.foosballcompanion.model.User
import no.kristo.foosballcompanion.ui.view.custom.CircleTransform

/**
 * Displays the main profile view
 * Created by Kristian on 09.10.2017.
 */


class ProfileView(context: Context?) : LinearLayout(context), DefaultLifecycleObserver {

    private var signout: () -> Unit = {}

    init {
        View.inflate(getContext(), R.layout.view_profile, this)
    }

    fun renderUser(user: User) {
        Picasso.with(context).load(user.photoUrl).transform(CircleTransform()).into(profileImage)
        profileName.text = user.displayName
        signOutButton.setOnClickListener { signout?.invoke() }
    }

    fun onSignoutClicked(body: () -> Unit) {
        signout = body
    }

}