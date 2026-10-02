package com.example.watsapporder.data.local

import com.example.watsapporder.data.mappers.LoggedUser

interface SessionStore {
    fun save(session: LoggedUser)
    fun get(): LoggedUser?
    fun saveFederatedPassword(email: String, password: String)
    fun getFederatedPassword(email: String): String?
    fun clear()
}
