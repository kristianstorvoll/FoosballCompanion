package no.kristo.foosballcompanion.ui.onboarding

import android.app.Activity
import android.content.Intent
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import no.kristo.foosballcompanion.model.User
import timber.log.Timber
import no.kristo.foosballcompanion.model.UserStore


/**
 * Created by Kristian on 08.10.2017.
 */
abstract class LoginDelegate(val onComplete: LoginDelegateListener) {

    protected val fireAuth by lazy { FirebaseAuth.getInstance() }

    abstract fun signIn()
    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    interface LoginDelegateListener {
        val complete: (Boolean) -> Unit
        val error: (Throwable) -> Unit
    }
}

class FacebookLoginDelegate(val activity: Activity, onComplete: LoginDelegate.LoginDelegateListener) : LoginDelegate(onComplete) {

    val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    val loginManager: LoginManager by lazy { LoginManager.getInstance() }

    val permissions: Collection<String>

    init {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onCancel() {
                Timber.d("Facebook login cancelled")
            }

            override fun onError(error: FacebookException?) {
                Timber.e(error)
            }

            override fun onSuccess(result: LoginResult?) {
                Timber.d("Successfully logged in: ${result.toString()}")
                handleResult(result?.accessToken)
            }

        })
        permissions = arrayOf("email", "public_profile").asList()
    }

    private fun handleResult(accessToken: AccessToken?) {
        if (accessToken != null) {

            val authCredential = FacebookAuthProvider.getCredential(accessToken?.token ?: "")

            fireAuth.signInWithCredential(authCredential).addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.d("signInWithCredential:success")
                    onComplete.complete(true)
                } else {
                    Timber.e("signInWithCredential:failure", it.exception)
                    onComplete.error(it.exception!!)
                }
            }

        }
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