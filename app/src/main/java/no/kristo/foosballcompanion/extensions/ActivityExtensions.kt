package no.kristo.foosballcompanion.extensions

import android.support.v7.app.AppCompatActivity
import android.widget.Toast


fun AppCompatActivity.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

val AppCompatActivity.onResultObservers: MutableList<ActivityResultsObserver>
    get() {
        return ArrayList()
    }



interface ActivityResultsObserver {
    fun onPermissionResult(requestCode: Int, permission: Array<String>, grantResults: Array<Int>) {}
}