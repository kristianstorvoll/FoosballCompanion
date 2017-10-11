package no.kristo.foosballcompanion.ui.devices

import android.arch.lifecycle.*
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_my_devices.view.*
import no.kristo.foosballcompanion.R
import no.kristo.foosballcompanion.data.UserDataRepository
import no.kristo.foosballcompanion.model.AppModel
import no.kristo.foosballcompanion.model.NfcDevice
import no.kristo.foosballcompanion.model.UserData

/**
 * Created by Kristian on 09.10.2017.
 */

interface AddNfcDeviceDelegate  {
    fun addDevice(completion: (NfcDevice) -> Unit)
}

class MyDevicesView(context: Context) : LinearLayout(context), DefaultLifecycleObserver {

    private lateinit var vm: MyDevicesViewModel

    var addDeviceDelegate: AddNfcDeviceDelegate? = null

    var checkNfcAvailabilityBlock: (() -> Unit)? = null

    private fun renderMyDevices(devices: List<NfcDevice>) {
        var text: String = ""
        devices.forEach { text+="${it.id} -> ${it.description} \n" }
        devicesTextView.text = text
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        inflate(context, R.layout.view_my_devices, this)
        if (owner !is FragmentActivity) return

        setupUi()
        vm = ViewModelProviders.of(owner).get(MyDevicesViewModel::class.java)
    }

    fun setupUi() {
        checkNfcButton.setOnClickListener { checkNfcAvailabilityBlock?.invoke() }
        addDeviceButton.setOnClickListener { addNewDeviceToUser() }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        vm.getMyDevices { error, userData ->
            userData?.devices?.let { renderMyDevices(it) }
        }
    }

    private fun addNewDeviceToUser() {
        addDeviceDelegate?.addDevice {
            vm.addNewDevice(it) { err, data ->
                data?.devices?.let { renderMyDevices(it) }
            }
        }
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }
}



class MyDevicesViewModel(val userId: String = AppModel.instance.currentUser?.userUid ?: "") : ViewModel() {

    val userRepository by lazy { UserDataRepository.instance }

    fun getMyDevices(onLoad: (Error?, UserData?) -> Unit) {
        userRepository.getUserProfile(userId, onLoad)
    }

    fun addNewDevice(device: NfcDevice, completion: (Error?, UserData?) -> Unit) {
        userRepository.addDevice(userId, device, completion)
    }

    override fun onCleared() {
        super.onCleared()
        //TODO: Handle this by clearing any callbacks (blocks/lambdas) that might cause a leak
    }
}
