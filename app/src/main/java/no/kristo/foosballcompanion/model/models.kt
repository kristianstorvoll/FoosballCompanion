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

object UserStore {
    var user: User? = null
}