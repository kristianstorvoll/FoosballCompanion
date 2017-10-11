package no.kristo.foosballcompanion.nfc

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.cardemulation.NfcFCardEmulation
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import no.kristo.foosballcompanion.extensions.ActivityResultsObserver


/**
 * Created by Kristian on 11.10.2017.
 */




abstract class NfcDelgate : ActivityResultsObserver{

    val CHECK_NFC_REQUEST_CODE = 1

    open fun isNfcAvailable(): Boolean {
        return false;
    }
}

class NfcDelegateImpl(var activity: FragmentActivity) : NfcDelgate() {

    val nfcAdapter: NfcAdapter? by lazy { NfcAdapter.getDefaultAdapter(activity) }

    fun checkNfcPermission() {
        val nfcPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.NFC)

        if (nfcPermission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.NFC)) {
                //ask for description
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.NFC), CHECK_NFC_REQUEST_CODE)
            }
        } else {

        }
    }

    override fun isNfcAvailable(): Boolean {
        return nfcAdapter != null
    }

    override fun onPermissionResult(requestCode: Int, permission: Array<String>, grantResults: Array<Int>) {
        when(requestCode) {
            CHECK_NFC_REQUEST_CODE -> {
                if (grantResults.size > 0) grantResults[0].let { it == PackageManager.PERMISSION_GRANTED }.let {
                    //ok we have access
                    openSensor();
                }
            }
        }
    }


    fun openSensor() {

    }

}