package no.kristo.foosballcompanion.extensions

import android.support.v7.app.AppCompatActivity
import android.widget.Toast


fun AppCompatActivity.toast(message: String): Unit {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
}