package no.kristo.foosballcompanion.data

import no.kristo.foosballcompanion.model.User

/**
 * Created by Kristian on 08.10.2017.
 */
interface UserService {
    fun getUser(): User
}