package no.kristo.foosballcompanion.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Generate a hash string for current debug key.
 * E.g: Used by facebook-sdk to validate app
 */
fun generateHash(context: Context) {
    try {
        val info = context.packageManager.getPackageInfo(
                "no.kristo.foosballcompanion",
                PackageManager.GET_SIGNATURES)
        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            val key = Base64.encodeToString(md.digest(), Base64.DEFAULT)
            Timber.d("KeyHash: " + key)
            //Log.d("OnboardingActivity", "KeyHash: " + key)
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
}