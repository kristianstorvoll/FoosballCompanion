package no.kristo.foosballcompanion.model

enum class LoginProvider(value: String) {
    FACEBOOK("facebook.com"),
    TWITTER("twitter")
}

data class User(
        val userUid: String = "",
        val displayName: String = "",
        val email: String = "",
        val photoUrl: String = "",
        val provider: LoginProvider
)


data class UserData(var devices: MutableList<NfcDevice> = ArrayList())

data class NfcDevice(var id: String = "", var description: String = "")