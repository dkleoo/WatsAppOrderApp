package com.example.watsapporder.data.local

import com.example.watsapporder.data.mappers.LoggedUser

class InMemorySessionStore : SessionStore {
    private var session: LoggedUser? = null
    private val federatedPasswords = mutableMapOf<String, String>()
    private var lastOrderSequence: Long = 0L

    override fun save(session: LoggedUser) {
        this.session = session
    }

    override fun get(): LoggedUser? = session

    override fun saveFederatedPassword(email: String, password: String) {
        federatedPasswords[email] = password
    }

    override fun getFederatedPassword(email: String): String? = federatedPasswords[email]

    override fun saveLastOrderSequence(sequence: Long) {
        lastOrderSequence = sequence
    }

    override fun getLastOrderSequence(): Long = lastOrderSequence

    override fun clear() {
        session = null
        federatedPasswords.clear()
        lastOrderSequence = 0L
    }
}
