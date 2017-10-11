package no.kristo.foosballcompanion.ui.onboarding

import android.app.Activity
import android.content.Intent
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber


/**
 * Created by Kristian on 08.10.2017.
 */
abstract class LoginDelegate(val listener: LoginDelegateListener) {

    protected val fireAuth by lazy { FirebaseAuth.getInstance() }

    abstract fun signIn()
    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    interface LoginDelegateListener {
        val complete: (Boolean) -> Unit
        val error: (Throwable) -> Unit
    }

    protected fun handleResult(token: String?) {
        val authCredential = FacebookAuthProvider.getCredential(token ?: "")

        fireAuth.signInWithCredential(authCredential).addOnFailureListener {
            Timber.e(it)
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                Timber.d("signInWithCredential:success")
                listener.complete(true)
            } else {
                Timber.e("signInWithCredential:failure", it.exception)
                listener.error(it.exception!!)
            }
        }
    }

    protected fun handleCancel() {
        //no implementation yet
    }

    protected fun handleError(error: Throwable) {
        listener.error(error)
    }
}

class FacebookLoginDelegate(val activity: Activity, listener: LoginDelegateListener) : LoginDelegate(listener) {

    val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    val loginManager: LoginManager by lazy { LoginManager.getInstance() }

    init {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onCancel() {
                Timber.d("Facebook login cancelled")
                handleCancel()
            }

            override fun onError(error: FacebookException?) {
                Timber.e(error, "FacebookLoginDelegate onError")
                error?.let { handleError(it) }
            }

            override fun onSuccess(result: LoginResult?) {
                result?.let { handleResult(it.accessToken.token) }
            }

        })
    }

    override fun signIn() {
        loginManager.logInWithReadPermissions(activity, createReadPermissions())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun createReadPermissions(): Collection<String> {
        return arrayOf("email", "public_profile").asList()
    }
}