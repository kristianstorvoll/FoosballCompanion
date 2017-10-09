package no.kristo.foosballcompanion.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import timber.log.Timber

/**
 * Created by Kristian on 08.10.2017.
 */

class AppModel {

    companion object {
        val instance: AppModel = AppModel()
    }

    private var userRelay: BehaviorRelay<User> = BehaviorRelay.create()
    private var isConfigured = false
    private val fireAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    /**
     * The current user object
     */
    var currentUser: User? = null


    /**
     * Observe the current user
     */
    fun currentUserObservable(): Observable<User> {
        return userRelay.take(1)
    }

    /**
     * Configure the app model. Optional completion block executed with [this] as parameter
     */
    fun configure(callback: ((AppModel) -> Unit)?): Unit? {
        if (isConfigured) {
            return callback?.invoke(this)
        }

        if (fireAuth.currentUser != null) {
            setCurrentUser(fireAuth.currentUser)
        }

        fireAuth.addAuthStateListener {
            Timber.d("AppModel -> Auth State changed")
            setCurrentUser(it.currentUser)
        }

        return callback?.invoke(this)
    }

    private fun setCurrentUser(user: FirebaseUser?) {
        if (currentUser?.userUid.equals(user?.uid)) {
            return
        }
        if (user == null) {
            currentUser = null
        } else {
            currentUser = User(user.uid, user.displayName!!, user.email!!, user.photoUrl?.toString() ?: "", LoginProvider.FACEBOOK)
            userRelay.accept(currentUser)
        }
    }

}