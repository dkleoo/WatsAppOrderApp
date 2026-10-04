package com.example.watsapporder.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.watsapporder.data.mappers.AuthProvider
import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.platform.ActivityHolder

class AndroidSessionStore : SessionStore {

    private val prefs: SharedPreferences
        get() {
            val context = ActivityHolder.application
                ?: ActivityHolder.current
                ?: error("Contexto no disponible")
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }

    override fun save(session: LoggedUser) {
        prefs.edit()
            .putString(KEY_TOKEN, session.token)
            .putString(KEY_ID, session.id)
            .putString(KEY_NAME, session.name)
            .putString(KEY_EMAIL, session.email)
            .putString(KEY_PROVIDER, session.provider.name)
            .putString(KEY_PHOTO, session.photoUrl)
            .putInt(KEY_STORE_ID, session.storeId ?: STORE_ID_NONE)
            .apply()
    }

    override fun get(): LoggedUser? {
        val token = prefs.getString(KEY_TOKEN, null) ?: return null
        val storeId = prefs.getInt(KEY_STORE_ID, STORE_ID_NONE).takeIf { it != STORE_ID_NONE }
        return LoggedUser(
            id = prefs.getString(KEY_ID, "").orEmpty(),
            name = prefs.getString(KEY_NAME, "").orEmpty(),
            email = prefs.getString(KEY_EMAIL, "").orEmpty(),
            token = token,
            provider = runCatching {
                AuthProvider.valueOf(prefs.getString(KEY_PROVIDER, "").orEmpty())
            }.getOrDefault(AuthProvider.EMAIL),
            photoUrl = prefs.getString(KEY_PHOTO, null),
            storeId = storeId,
        )
    }

    override fun saveFederatedPassword(email: String, password: String) {
        prefs.edit().putString(passwordKey(email), password).apply()
    }

    override fun getFederatedPassword(email: String): String? =
        prefs.getString(passwordKey(email), null)

    override fun clear() {
        prefs.edit().clear().apply()
    }

    private fun passwordKey(email: String): String = "federated_password_$email"

    private companion object {
        const val PREFS_NAME = "watsapporder_session"
        const val KEY_TOKEN = "token"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_EMAIL = "email"
        const val KEY_PROVIDER = "provider"
        const val KEY_PHOTO = "photo_url"
        const val KEY_STORE_ID = "store_id"
        const val STORE_ID_NONE = -1
    }
}
