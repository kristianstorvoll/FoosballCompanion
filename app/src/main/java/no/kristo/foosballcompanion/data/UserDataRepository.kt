package no.kristo.foosballcompanion.data

import com.google.firebase.firestore.*
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import no.kristo.foosballcompanion.model.NfcDevice
import no.kristo.foosballcompanion.model.UserData
import timber.log.Timber

/**
 * Created by Kristian on 08.10.2017.
 */
interface UserDataRepository {

    fun getUserProfile(userId: String): Observable<UserData>
    fun getUserProfile(userId: String, after: (Error?, UserData?) -> Unit)
    fun updateUserData(userId: String, data: UserData, completion: (Error? , UserData?) -> Unit)

    companion object {
        val instance: UserDataRepository = FirebaseUserDataRepository()
    }

    fun addDevice(userId: String, deviceId: NfcDevice, completion: (Error?, UserData?) -> Unit) {}
}

class FirebaseUserDataRepository internal constructor(): UserDataRepository {

    var userProfile: UserData? = null

    val userDataRelay: Observable<UserData> = BehaviorRelay.create()

    private var addSnapshotListener: ListenerRegistration? = null

    private fun setupDataListener(userId: String) {
        addSnapshotListener = if (addSnapshotListener != null) addSnapshotListener else //create it
            getUserDataReference(userId).addSnapshotListener(object : EventListener<DocumentSnapshot> {
                override fun onEvent(p0: DocumentSnapshot?, p1: FirebaseFirestoreException?) {
                    Timber.d("Document changed!")
                }
            })
    }


    private fun getUserDataReference(userId: String): DocumentReference {
        return db.collection("user_data").document(userId)
    }

    override fun getUserProfile(userId: String, after: (Error?, UserData?) -> Unit) {
        userProfile?.let { return after(null, it) }

        getUserDataReference(userId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = if (it.result.exists()) it.result.toObject(UserData::class.java) else null
                userProfile = result
                after(null, result)
            } else {
                after(Error(it.exception), null)
            }
        }
    }

    override fun getUserProfile(userId: String): Observable<UserData> {
        setupDataListener(userId)
        return userDataRelay
    }

    val db by lazy { FirebaseFirestore.getInstance() }

    override fun updateUserData(userId: String, data: UserData, completion: (Error?, UserData?) -> Unit) {
        db.collection("user_data").document(userId).set(data)
                .addOnCompleteListener { completion(null, data) }
                .addOnFailureListener { completion(Error(it), null) }
    }

    override fun addDevice(userId: String, device: NfcDevice, completion: (Error?, UserData?) -> Unit) {
        userProfile?.also { it.devices.add(device) }?.let {  updateUserData(userId, it, completion) }
    }

}