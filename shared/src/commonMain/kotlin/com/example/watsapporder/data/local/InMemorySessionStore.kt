package com.example.watsapporder.data.local

import com.example.watsapporder.data.mappers.LoggedUser

class InMemorySessionStore : SessionStore {
    private var session: LoggedUser? = null
    private val federatedPasswords = mutableMapOf<String, String>()

    override fun save(session: LoggedUser) {
        this.session = session
    }

    override fun get(): LoggedUser? = session

    override fun saveFederatedPassword(email: String, password: String) {
        federatedPasswords[email] = password
    }

    override fun getFederatedPassword(email: String): String? = federatedPasswords[email]

    override fun clear() {
        session = null
        federatedPasswords.clear()
    }
}
