package no.kristo.foosballcompanion

import android.app.Application
import no.kristo.foosballcompanion.model.AppModel
import timber.log.Timber

/**
 * Created by Kristian on 08.10.2017.
 */
class FoosballApp : Application() {

    companion object {
        lateinit var instance: FoosballApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        setupLogging()
        instance = this

        AppModel.instance.configure(null)
    }

    fun setupLogging() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}